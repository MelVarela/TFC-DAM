package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalPlace
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemotePlace
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CampaignDao
import com.example.notasmazmorras.data.repositories.daos.NoteDao
import com.example.notasmazmorras.data.repositories.daos.PlaceDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface PlaceRepository {

    fun getAllPlaces(): Flow<List<LocalPlace>>

    suspend fun insertPlace(place: LocalPlace): RepositoryResult

    suspend fun updatePlace(place: LocalPlace): RepositoryResult

    suspend fun deletePlace(place: LocalPlace): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(campaignId: String): RepositoryResult

    suspend fun reset()
}

class DefaultPlaceRepository(
    private val local : PlaceDao,
    private val notes : NoteDao,
    private val remote : ApiService
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
            local.update(place.copy(pendingSync = true))
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
        var toSync = local.getPlacesToSync()

        try{
            toSync.first().map { place ->
                val id = place.id.substring(place.id.indexOf("_") + 1, place.id.length)

                if(place.pendingDelete){
                    if(place.id.substring(0, 1) != "l") remote.deletePlace(place.id)
                    local.delete(place)
                }else if(place.id.substring(0, 1) == "l" && place.campaign.substring(0, 1) != "l"){

                    var resposta : RemotePlace =
                        remote.createPlace(place.toRemote())

                    local.updateLocal((resposta.id ?: place.id), place.id)

                    val nots = notes.getByOwner(place.id).first()

                    nots.map { note ->
                        notes.update(
                            note.copy(
                                owner = (resposta.id ?: place.id)
                            )
                        )
                    }

                }else{

                    if(place.id.substring(0, 1) != "l"){
                        remote.updatePlace(place.toRemote())
                        local.update(place.copy(pendingSync = false))
                    }

                }
            }
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
        }

        return RepositoryResult.Success("Cambios sincronizados con éxito.")
    }

    override suspend fun syncFromServer(campaignId: String): RepositoryResult {
        try{
            val places = remote.getPlaces(campaignId)
            val ids = local.getIds().first()
            var workedIds : List<String> = emptyList()

            var placesToUpdate : List<LocalPlace> = ArrayList<LocalPlace>()
            var placesToInsert : List<LocalPlace> = ArrayList<LocalPlace>()

            places.map {
                if(!(ids.contains(it.id))){
                    placesToInsert = placesToInsert.plus(it.toLocal())
                    workedIds = workedIds.plus(it.id ?: "")
                }else{
                    placesToUpdate = placesToUpdate.plus(it.toLocal())
                    workedIds = workedIds.plus(it.id ?: "")
                }
            }
            ids.map {
                if(!workedIds.contains(it) && it.substring(0, 1) != "l") local.deleteById(it)
            }

            local.insertList(placesToInsert)
            local.updateList(placesToUpdate)
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