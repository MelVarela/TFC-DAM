package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.data.repositories.daos.UserRelationDao
import kotlinx.coroutines.flow.Flow

interface UserRelationRepository {

    fun getAllUserRelations(): Flow<List<LocalUserRelation>>

    suspend fun insertUserRelation(userRelation: LocalUserRelation): RepositoryResult

    suspend fun updateUserRelation(userRelation: LocalUserRelation): RepositoryResult

    suspend fun deleteUserRelation(userRelation: LocalUserRelation): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(): RepositoryResult
}

class DefaultUserRelationRepository(
    private val local : UserRelationDao,
    private val remote : Object //ApiService
) : UserRelationRepository {

    final val TAG = "userRelation_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllUserRelations(): Flow<List<LocalUserRelation>> = local.getAllRelations()

    override suspend fun insertUserRelation(userRelation: LocalUserRelation): RepositoryResult {
        try{
            local.insert(userRelation.copy(pendingSync = true))
            return RepositoryResult.Success("")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("")
        }
    }

    override suspend fun updateUserRelation(userRelation: LocalUserRelation): RepositoryResult {
        try{
            local.update(userRelation)
            return RepositoryResult.Success("")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("")
        }
    }

    override suspend fun deleteUserRelation(userRelation: LocalUserRelation): RepositoryResult {
        try{
            updateUserRelation(userRelation.copy(pendingSync = true, pendingDelete = true))
            return RepositoryResult.Success("")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("")
        }
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        throw Exception("To Do")
    }

    override suspend fun syncFromServer(): RepositoryResult {
        throw Exception("To Do")
    }

}