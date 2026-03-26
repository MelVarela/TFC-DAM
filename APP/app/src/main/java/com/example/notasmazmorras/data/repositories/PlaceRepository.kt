package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalPlace
import com.example.notasmazmorras.data.repositories.daos.CampaignDao
import com.example.notasmazmorras.data.repositories.daos.PlaceDao
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {

    fun getAllPlaces(): Flow<List<LocalPlace>>

    suspend fun insertPlace(place: LocalPlace): RepositoryResult

    suspend fun updatePlace(place: LocalPlace): RepositoryResult

    suspend fun deletePlace(place: LocalPlace): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(): RepositoryResult
}

class DefaultPlaceRepository(
    private val local : PlaceDao,
    private val remote : Object //ApiService
) : PlaceRepository {

    final val TAG = "place_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllPlaces(): Flow<List<LocalPlace>> = local.getAllPlaces()

    override suspend fun insertPlace(place: LocalPlace): RepositoryResult {
        try{
            local.insert(place.copy(pendingSync = true))
            return RepositoryResult.Success("Lugar '${place.name}' creado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error creando el lugar '${place.name}'.")
        }
    }

    override suspend fun updatePlace(place: LocalPlace): RepositoryResult {
        try{
            local.update(place)
            return RepositoryResult.Success("Lugar '${place.name}' actualizado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            if(place.pendingDelete) return RepositoryResult.Error("Error eliminando el lugar '${place.name}'.")
            else return RepositoryResult.Error("Error actualizando el lugar '${place.name}'.")
        }
    }

    override suspend fun deletePlace(place: LocalPlace): RepositoryResult {
        try{
            updatePlace(place.copy(pendingSync = true, pendingDelete = true))
            return RepositoryResult.Success("Lugar '${place.name}' eliminado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error eliminando el lugar '${place.name}'.")
        }
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        throw Exception("To Do")
    }

    override suspend fun syncFromServer(): RepositoryResult {
        throw Exception("To Do")
    }

}