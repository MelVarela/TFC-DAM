package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCampaign
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CampaignDao
import com.example.notasmazmorras.data.repositories.daos.CharacterDao
import com.example.notasmazmorras.data.repositories.daos.CreatureDao
import com.example.notasmazmorras.data.repositories.daos.NoteDao
import com.example.notasmazmorras.data.repositories.daos.ObjectDao
import com.example.notasmazmorras.data.repositories.daos.PlaceDao
import com.example.notasmazmorras.data.repositories.daos.UserRelationDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.collections.map

interface CampaignRepository {

    fun getAllCampaigns(): Flow<List<LocalCampaign>>

    suspend fun insertCampaign(campaign: LocalCampaign): RepositoryResult

    suspend fun updateCampaign(campaign: LocalCampaign): RepositoryResult

    suspend fun deleteCampaign(campaign: LocalCampaign): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(email: String): RepositoryResult

    suspend fun reset()
}

class DefaultCampaignRepository(
    private val local : CampaignDao,
    private val notes : NoteDao,
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
            toSync.first().map { campaign ->

                if(campaign.pendingDelete){

                    local.delete(campaign)
                    if(campaign.id.substring(0, 1) != "l") remote.deleteCampaign(campaign.id)

                }else if(campaign.id.substring(0, 1) == "l"){

                    val resposta : RemoteCampaign =
                        remote.createCampaign(campaign.toRemote())

                    local.updateLocal((resposta.id ?: campaign.id), campaign.id)

                    val nots = notes.getByOwner(campaign.id).first()

                    nots.map { note ->
                        notes.update(
                            note.copy(
                                owner = (resposta.id ?: campaign.id)
                            )
                        )
                    }

                }else{

                    remote.updateCampaign(campaign.toRemote())
                    local.update(campaign.copy(pendingSync = false))

                }
            }
        }catch (e : Throwable){
            Log.e(TAG, ("${e.message ?: NO_ERR}: \n${Log.getStackTraceString(e)}"))
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }

        return RepositoryResult.Success("Cambios sincronizados con éxito.")
    }

    override suspend fun syncFromServer(email: String): RepositoryResult {
        try{
            val campaigns = remote.getCampaigns(email)
            val ids = local.getIds().first()
            var workedIds : List<String> = emptyList()

            var campaignsToUpdate : List<LocalCampaign> = ArrayList<LocalCampaign>()
            var campaignsToInsert : List<LocalCampaign> = ArrayList<LocalCampaign>()

            campaigns.map {
                if(!(ids.contains(it.id))){
                    campaignsToInsert = campaignsToInsert.plus(it.toLocal())
                    workedIds = workedIds.plus(it.id ?: "")
                }else{
                    campaignsToUpdate = campaignsToUpdate.plus(it.toLocal())
                    workedIds = workedIds.plus(it.id ?: "")
                }
            }
            ids.map {
                if(!workedIds.contains(it)) local.deleteById(it)
            }

            local.insertList(campaignsToInsert)
            local.updateList(campaignsToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

    override suspend fun reset() {
        local.deleteAll()
    }

}