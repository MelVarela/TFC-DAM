package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteCreature
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CreatureDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface CreatureRepository {

    fun getAllCreatures(): Flow<List<LocalCreature>>

    suspend fun insertCreature(creature: LocalCreature): RepositoryResult

    suspend fun updateCreature(creature: LocalCreature): RepositoryResult

    suspend fun deleteCreature(creature: LocalCreature): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(campaignId: String): RepositoryResult
}

class DefaultCreatureRepository(
    private val local : CreatureDao,
    private val remote : ApiService
) : CreatureRepository {

    final val TAG = "creature_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllCreatures(): Flow<List<LocalCreature>> = local.getAllCreatures()

    override suspend fun insertCreature(creature: LocalCreature): RepositoryResult {
        try{
            local.insert(creature.copy(pendingSync = true))
            return RepositoryResult.Success("Criatura '${creature.name}' creada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error creando la criatura '${creature.name}'.")
        }
    }

    override suspend fun updateCreature(creature: LocalCreature): RepositoryResult {
        try{
            local.update(creature)
            return RepositoryResult.Success("Criatura '${creature.name}' actualizada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            if(creature.pendingDelete) return RepositoryResult.Error("Error eliminando la criatura '${creature.name}'.")
            else return RepositoryResult.Error("Error actualizando la criatura '${creature.name}'.")
        }
    }

    override suspend fun deleteCreature(creature: LocalCreature): RepositoryResult {
        try{
            updateCreature(creature.copy(pendingSync = true, pendingDelete = true))
            return RepositoryResult.Success("Criatura '${creature.name}' eliminada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error eliminando la criatura '${creature.name}'.")
        }
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        var toSync = local.getCreaturesToSync()

        try{
            toSync.first().map {
                val id = it.id.substring(it.id.indexOf("_") + 1, it.id.length)

                if(it.pendingDelete){
                    if(!(it.id.substring(0, 1) == "l")) remote.deleteCreature(it.toRemote())
                    local.delete(it)
                }else if(it.id.substring(0, 1) == "l"){

                    var resposta : RemoteCreature =
                        remote.createCreature(it.toRemote())
                    local.delete(it)
                    local.insert(it.copy((resposta.id ?: "0"), pendingSync = false))

                }else{

                    remote.updateCreature(it.toRemote())
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
            var creatures = remote.getCreatures(campaignId)
            var ids = local.getIds()

            var creaturesToUpdate : List<LocalCreature> = ArrayList<LocalCreature>()
            var creaturesToInsert : List<LocalCreature> = ArrayList<LocalCreature>()

            creatures.map {
                if(!(ids.first().contains(it.id))){
                    creaturesToInsert = creaturesToInsert.plus(it.toLocal())
                }else{
                    creaturesToUpdate = creaturesToUpdate.plus(it.toLocal())
                }
            }

            local.insertList(creaturesToInsert)
            local.updateList(creaturesToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

}