package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteObject
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.ObjectDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface ObjectRepository {

    fun getAllObjects(): Flow<List<LocalObject>>

    suspend fun insertObject(obxecto: LocalObject): RepositoryResult

    suspend fun updateObject(obxecto: LocalObject): RepositoryResult

    suspend fun deleteObject(obxecto: LocalObject): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(campaignId: String): RepositoryResult
}

class DefaultObjectRepository(
    private val local : ObjectDao,
    private val remote : ApiService
) : ObjectRepository {

    final val TAG = "object_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllObjects(): Flow<List<LocalObject>> = local.getAllObjects()

    override suspend fun insertObject(obxecto: LocalObject): RepositoryResult {
        try{
            local.insert(obxecto.copy(pendingSync = true))
            return RepositoryResult.Success("Objeto '${obxecto.name}' creado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error creando el objeto '${obxecto.name}'.")
        }
    }

    override suspend fun updateObject(obxecto: LocalObject): RepositoryResult {
        try{
            local.update(obxecto.copy(pendingSync = true))
            return RepositoryResult.Success("Objeto '${obxecto.name}' actualizado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            if(obxecto.pendingDelete) return RepositoryResult.Error("Error eliminando el objeto '${obxecto.name}'.")
            else return RepositoryResult.Error("Error actualizando el objeto '${obxecto.name}'.")
        }
    }

    override suspend fun deleteObject(obxecto: LocalObject): RepositoryResult {
        try{
            updateObject(obxecto.copy(pendingSync = true, pendingDelete = true))
            return RepositoryResult.Success("Objeto '${obxecto.name}' eliminado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error eliminando el objeto '${obxecto.name}'.")
        }
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        var toSync = local.getObjectsToSync()

        try{
            toSync.first().map {
                val id = it.id.substring(it.id.indexOf("_") + 1, it.id.length)

                if(it.pendingDelete){
                    if(it.id.substring(0, 1) != "l") remote.deleteObject(it.id)
                    local.delete(it)
                }else if(it.id.substring(0, 1) == "l"){

                    var resposta : RemoteObject =
                        remote.createObject(it.toRemote())
                    local.delete(it)
                    local.insert(it.copy((resposta.id ?: "0"), pendingSync = false))

                }else{

                    remote.updateObject(it.toRemote())
                    local.update(it.copy(pendingSync = false))

                }
            }
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
        }

        return RepositoryResult.Success("Cambios sincronizados con éxito.")
    }

    override suspend fun syncFromServer(campaignId: String): RepositoryResult {
        try{
            var objects = remote.getObjects(campaignId)
            var ids = local.getIds()

            var objectsToUpdate : List<LocalObject> = ArrayList<LocalObject>()
            var objectsToInsert : List<LocalObject> = ArrayList<LocalObject>()

            objects.map {
                if(!(ids.first().contains(it.id))){
                    objectsToInsert = objectsToInsert.plus(it.toLocal())
                }else{
                    objectsToUpdate = objectsToUpdate.plus(it.toLocal())
                }
            }

            local.insertList(objectsToInsert)
            local.updateList(objectsToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

}