package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.data.repositories.daos.ObjectDao
import kotlinx.coroutines.flow.Flow

interface ObjectRepository {

    fun getAllObjects(): Flow<List<LocalObject>>

    suspend fun insertObject(obxecto: LocalObject): RepositoryResult

    suspend fun updateObject(obxecto: LocalObject): RepositoryResult

    suspend fun deleteObject(obxecto: LocalObject): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(): RepositoryResult
}

class DefaultObjectRepository(
    private val local : ObjectDao,
    private val remote : Object //ApiService
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
            local.update(obxecto)
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
        throw Exception("To Do")
    }

    override suspend fun syncFromServer(): RepositoryResult {
        throw Exception("To Do")
    }

}