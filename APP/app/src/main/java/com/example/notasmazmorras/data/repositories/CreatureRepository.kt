package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteCreature
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CreatureDao
import com.example.notasmazmorras.data.repositories.daos.NoteDao
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

    suspend fun reset()
}

class DefaultCreatureRepository(
    private val local : CreatureDao,
    private val notes : NoteDao,
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
            local.update(creature.copy(pendingSync = true))
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
            toSync.first().map { creature ->
            val id = creature.id.substring(creature.id.indexOf("_") + 1, creature.id.length)

                if(creature.pendingDelete){
                    if(creature.id.substring(0, 1) != "l") remote.deleteCreature(creature.id)
                    local.delete(creature)
                }else if(creature.id.substring(0, 1) == "l" && creature.campaign.substring(0, 1) != "l"){

                    var resposta : RemoteCreature =
                        remote.createCreature(creature.toRemote())

                    local.updateLocal((resposta.id ?: creature.id), creature.id)

                    val nots = notes.getByOwner(creature.id).first()

                    nots.map { note ->
                        notes.update(
                            note.copy(
                                owner = (resposta.id ?: creature.id)
                            )
                        )
                    }

                }else{

                    if(creature.id.substring(0, 1) != "l"){
                        remote.updateCreature(creature.toRemote())
                        local.update(creature.copy(pendingSync = false))
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
            val creatures = remote.getCreatures(campaignId)
            val ids = local.getIds().first()
            var workedIds : List<String> = emptyList()

            var creaturesToUpdate : List<LocalCreature> = ArrayList<LocalCreature>()
            var creaturesToInsert : List<LocalCreature> = ArrayList<LocalCreature>()

            creatures.map {
                if(!(ids.contains(it.id))){
                    creaturesToInsert = creaturesToInsert.plus(it.toLocal())
                    workedIds = workedIds.plus(it.id ?: "")
                }else{
                    creaturesToUpdate = creaturesToUpdate.plus(it.toLocal())
                    workedIds = workedIds.plus(it.id ?: "")
                }
            }
            ids.map {
                if(!workedIds.contains(it) && it.substring(0, 1) != "l") local.deleteById(it)
            }

            local.insertList(creaturesToInsert)
            local.updateList(creaturesToUpdate)
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