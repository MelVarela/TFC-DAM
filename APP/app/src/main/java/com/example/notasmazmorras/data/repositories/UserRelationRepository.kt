package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteUser
import com.example.notasmazmorras.data.model.remote.RemoteUserRelation
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.UserRelationDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface UserRelationRepository {

    fun getAllUserRelations(): Flow<List<LocalUserRelation>>

    fun getRelationsForCampaign(campaign: String): Flow<List<LocalUserRelation>>

    suspend fun insertUserRelation(userRelation: LocalUserRelation): RepositoryResult

    suspend fun updateUserRelation(userRelation: LocalUserRelation): RepositoryResult

    suspend fun deleteUserRelation(userRelation: LocalUserRelation): RepositoryResult

    suspend fun invitePlayer(userRelation: LocalUserRelation): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(campaignId: String): RepositoryResult

    suspend fun syncPending(user: String) : RepositoryResult
}

class DefaultUserRelationRepository(
    private val local : UserRelationDao,
    private val remote : ApiService
) : UserRelationRepository {

    final val TAG = "userRelation_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllUserRelations(): Flow<List<LocalUserRelation>> = local.getAllRelations()

    override fun getRelationsForCampaign(campaign: String): Flow<List<LocalUserRelation>> = local.getRelationsForCampaign(campaign)

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
            local.update(userRelation.copy(pendingSync = true))
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

    override suspend fun invitePlayer(userRelation: LocalUserRelation): RepositoryResult {
        try{
            if(checkEmailExists(userRelation.user)){
                local.insert(userRelation)
                remote.createRelation(userRelation.toRemote())
                return RepositoryResult.Success("")
            }
            return RepositoryResult.Error("")
        }catch (e: Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("")
        }
    }

    suspend fun checkEmailExists(email: String): Boolean {
        var userCheck : RemoteUser = remote.getUser(email)
        return (userCheck.name != "")
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        var toSync = local.getRelationsToSync()

        try{
            toSync.first().map {
                if(it.pendingDelete){
                    remote.deleteRelation(it.user + "-" + it.campaign)
                    local.delete(it)
                }else if(!it.existsRemote){

                    var resposta : RemoteUserRelation =
                        remote.createRelation(it.toRemote())
                    local.delete(it)
                    local.insert(it.copy(pendingSync = false, existsRemote = true))

                }else{

                    remote.updateRelation(it.toRemote())
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
            var relationsRemote = remote.getRelations(campaignId)
            var relationsLocal = local.getRelationsForCampaign(campaignId)

            var relationsToUpdate : List<LocalUserRelation> = ArrayList<LocalUserRelation>()
            var relationsToInsert : List<LocalUserRelation> = ArrayList<LocalUserRelation>()

            Log.d("SYNC", relationsRemote.toString())

            relationsRemote.map {
                Log.d("SYNC", it.toString())
                if(!relationsLocal.first().contains(it.toLocal())){
                    Log.d("SYNC", "Insert")
                    relationsToInsert = relationsToInsert.plus(it.toLocal())
                }else{
                    Log.d("SYNC", "Update")
                    relationsToUpdate = relationsToUpdate.plus(it.toLocal())
                }
            }

            Log.d("SYNC I", relationsToInsert.toString())
            Log.d("SYNC U", relationsToUpdate.toString())

            local.insertList(relationsToInsert)
            local.updateList(relationsToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

    override suspend fun syncPending(user: String): RepositoryResult{
        try{
            var relationsRemote = remote.getPendingInvites(user)
            var relationsLocal = local.getAllRelations()

            var relationsToUpdate : List<LocalUserRelation> = ArrayList<LocalUserRelation>()
            var relationsToInsert : List<LocalUserRelation> = ArrayList<LocalUserRelation>()

            Log.d("SYNC", relationsRemote.toString())

            relationsRemote.map {
                Log.d("SYNC", it.toString())
                if(!relationsLocal.first().contains(it.toLocal())){
                    Log.d("SYNC", "Insert")
                    relationsToInsert = relationsToInsert.plus(it.toLocal())
                }else{
                    Log.d("SYNC", "Update")
                    relationsToUpdate = relationsToUpdate.plus(it.toLocal())
                }
            }

            Log.d("SYNC I", relationsToInsert.toString())
            Log.d("SYNC U", relationsToUpdate.toString())

            local.insertList(relationsToInsert)
            local.updateList(relationsToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

}