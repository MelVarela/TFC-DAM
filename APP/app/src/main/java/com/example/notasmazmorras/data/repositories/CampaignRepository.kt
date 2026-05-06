package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCampaign
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CampaignDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface CampaignRepository {

    fun getAllCampaigns(): Flow<List<LocalCampaign>>

    suspend fun insertCampaign(campaign: LocalCampaign): RepositoryResult

    suspend fun updateCampaign(campaign: LocalCampaign): RepositoryResult

    suspend fun deleteCampaign(campaign: LocalCampaign): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(email: String): RepositoryResult
}

class DefaultCampaignRepository(
    private val local : CampaignDao,
    private val remote : ApiService
) : CampaignRepository {

    final val TAG = "campaign_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllCampaigns(): Flow<List<LocalCampaign>> = local.getAllCampaigns()

    override suspend fun insertCampaign(campaign: LocalCampaign): RepositoryResult {
        try{
            local.insert(campaign.copy(pendingSync = true))
            return RepositoryResult.Success("Campaña '${campaign.name}' creada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error creando la campaña '${campaign.name}'.")
        }
    }

    override suspend fun updateCampaign(campaign: LocalCampaign): RepositoryResult {
        try{
            local.update(campaign.copy(pendingSync = true))
            return RepositoryResult.Success("Campaña '${campaign.name}' actualizada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            if(campaign.pendingDelete) return RepositoryResult.Error("Error eliminando la campaña '${campaign.name}'.")
            else return RepositoryResult.Error("Error actualizando la campaña '${campaign.name}'.")
        }
    }

    override suspend fun deleteCampaign(campaign: LocalCampaign): RepositoryResult {
        try{
            updateCampaign(campaign.copy(pendingSync = true, pendingDelete = true))
            return RepositoryResult.Success("Campaña '${campaign.name}' eliminada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error eliminando la campaña '${campaign.name}'.")
        }
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        var toSync = local.getCampaignsToSync()

        try{
            toSync.first().map {

                if(it.pendingDelete){

                    if(it.id.substring(0, 1) != "l") remote.deleteCampaign(it.id)
                    local.delete(it)

                }else if(it.id.substring(0, 1) == "l"){

                    var resposta : RemoteCampaign =
                        remote.createCampaign(it.toRemote())
                    local.delete(it)
                    local.insert(it.copy((resposta.id ?: "0"), pendingSync = false))

                }else{

                    remote.updateCampaign(it.toRemote())
                    local.update(it.copy(pendingSync = false))

                }
            }
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }

        return RepositoryResult.Success("Cambios sincronizados con éxito.")
    }

    override suspend fun syncFromServer(email: String): RepositoryResult {
        try{
            var campaigns = remote.getCampaigns(email)
            var ids = local.getIds()

            var campaignsToUpdate : List<LocalCampaign> = ArrayList<LocalCampaign>()
            var campaignsToInsert : List<LocalCampaign> = ArrayList<LocalCampaign>()

            campaigns.map {
                if(!(ids.first().contains(it.id))){
                    campaignsToInsert = campaignsToInsert.plus(it.toLocal())
                }else{
                    campaignsToUpdate = campaignsToUpdate.plus(it.toLocal())
                }
            }

            local.insertList(campaignsToInsert)
            local.updateList(campaignsToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

}