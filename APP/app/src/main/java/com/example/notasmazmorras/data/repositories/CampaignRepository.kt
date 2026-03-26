package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.repositories.daos.CampaignDao
import kotlinx.coroutines.flow.Flow

interface CampaignRepository {

    fun getAllCampaigns(): Flow<List<LocalCampaign>>

    suspend fun insertCampaign(campaign: LocalCampaign): RepositoryResult

    suspend fun updateCampaign(campaign: LocalCampaign): RepositoryResult

    suspend fun deleteCampaign(campaign: LocalCampaign): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(): RepositoryResult
}

class DefaultCampaignRepository(
    private val local : CampaignDao,
    private val remote : Object //ApiService
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
            local.update(campaign)
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
        throw Exception("To Do")
    }

    override suspend fun syncFromServer(): RepositoryResult {
        throw Exception("To Do")
    }

}