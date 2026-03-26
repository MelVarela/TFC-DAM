package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCreature
import com.example.notasmazmorras.data.repositories.daos.CreatureDao
import kotlinx.coroutines.flow.Flow

interface CreatureRepository {

    fun getAllCreatures(): Flow<List<LocalCreature>>

    suspend fun insertCreature(creature: LocalCreature): RepositoryResult

    suspend fun updateCreature(creature: LocalCreature): RepositoryResult

    suspend fun deleteCreature(creature: LocalCreature): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(): RepositoryResult
}

class DefaultCreatureRepository(
    private val local : CreatureDao,
    private val remote : Object //ApiService
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
        throw Exception("To Do")
    }

    override suspend fun syncFromServer(): RepositoryResult {
        throw Exception("To Do")
    }

}