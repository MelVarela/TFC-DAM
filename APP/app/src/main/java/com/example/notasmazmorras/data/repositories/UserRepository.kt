package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteUser
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.UserDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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
    private val remote : ApiService
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
        var toSync = local.getUsersToSync()

        try{
            toSync.first().map {
                val id = it.email.substring(it.email.indexOf("_") + 1, it.email.length)

                if(it.pendingDelete){
                    if(!(it.email.substring(0, 1) == "l")) remote.deleteUser(id)
                    local.delete(it)
                }else if(it.email.substring(0, 1) == "l"){

                    var resposta : RemoteUser =
                        remote.createUser(it.toRemote())
                    local.delete(it)
                    local.insert(it.copy((resposta.email ?: "0"), pendingSync = false))

                }else{

                    remote.updateUser(it.email, it.toRemote())
                    local.update(it.copy(pendingSync = false))

                }
            }
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
        }

        return RepositoryResult.Success("Cambios sincronizados con éxito.")
    }

    override suspend fun syncFromServer(): RepositoryResult {
        try{
            var users = remote.getUsers()
            var ids = local.getUserEmails()

            var usersToUpdate : List<LocalUser> = ArrayList<LocalUser>()
            var usersToInsert : List<LocalUser> = ArrayList<LocalUser>()

            users.map {
                if(!(ids.first().contains(it.email))){
                    usersToInsert = usersToInsert.plus(it.toLocal())
                }else{
                    usersToUpdate = usersToUpdate.plus(it.toLocal())
                }
            }

            local.insertList(usersToInsert)
            local.updateList(usersToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

}