package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.repositories.daos.CharacterDao
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getAllCharacters(): Flow<List<LocalCharacter>>

    suspend fun insertCharacter(character: LocalCharacter): RepositoryResult

    suspend fun updateCharacter(character: LocalCharacter): RepositoryResult

    suspend fun deleteCharacter(character: LocalCharacter): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(): RepositoryResult
}

class DefaultCharacterRepository(
    private val local : CharacterDao,
    private val remote : Object //ApiService
) : CharacterRepository {

    final val TAG = "character_repository"
    final val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllCharacters(): Flow<List<LocalCharacter>> = local.getAllCharacters()

    override suspend fun insertCharacter(character: LocalCharacter): RepositoryResult {
        try{
            local.insert(character.copy(pendingSync = true))
            return RepositoryResult.Success("Personaje '${character.name}' creado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error creando el personaje '${character.name}'.")
        }
    }

    override suspend fun updateCharacter(character: LocalCharacter): RepositoryResult {
        try{
            local.update(character)
            return RepositoryResult.Success("Personaje '${character.name}' actualizado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            if(character.pendingDelete) return RepositoryResult.Error("Error eliminando el personaje '${character.name}'.")
            else return RepositoryResult.Error("Error actualizando el personaje '${character.name}'.")
        }
    }

    override suspend fun deleteCharacter(character: LocalCharacter): RepositoryResult {
        try{
            updateCharacter(character.copy(pendingSync = true, pendingDelete = true))
            return RepositoryResult.Success("Personaje '${character.name}' eliminado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error eliminando el personaje '${character.name}'.")
        }
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        throw Exception("To Do")
    }

    override suspend fun syncFromServer(): RepositoryResult {
        throw Exception("To Do")
    }

}