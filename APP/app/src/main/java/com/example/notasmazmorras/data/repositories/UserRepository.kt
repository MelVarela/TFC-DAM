package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.repositories.daos.UserDao
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUsers(): Flow<List<LocalUser>>

    suspend fun insertUser(user: LocalUser): RepositoryResult

    suspend fun updateUser(user: LocalUser): RepositoryResult

    suspend fun deleteUser(user: LocalUser): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(): RepositoryResult
}

class DefaultUserRepository(
    private val local : UserDao,
    private val remote : Object //ApiService
) : UserRepository {

    final val TAG = "user_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllUsers(): Flow<List<LocalUser>> = local.getAllUsers()

    override suspend fun insertUser(user: LocalUser): RepositoryResult {
        try{
            local.insert(user.copy(pendingSync = true))
            return RepositoryResult.Success("")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("")
        }
    }

    override suspend fun updateUser(user: LocalUser): RepositoryResult {
        try{
            local.update(user)
            return RepositoryResult.Success("")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("")
        }
    }

    override suspend fun deleteUser(user: LocalUser): RepositoryResult {
        try{
            updateUser(user.copy(pendingSync = true, pendingDelete = true))
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