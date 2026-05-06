package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.RemoteCampaign
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CharacterDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface CharacterRepository {

    fun getAllCharacters(): Flow<List<LocalCharacter>>

    suspend fun insertCharacter(character: LocalCharacter): RepositoryResult

    suspend fun updateCharacter(character: LocalCharacter): RepositoryResult

    suspend fun deleteCharacter(character: LocalCharacter): RepositoryResult

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(campaignId : String): RepositoryResult
}

class DefaultCharacterRepository(
    private val local : CharacterDao,
    private val remote : ApiService
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
            local.update(character.copy(pendingSync = true))
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
        var toSync = local.getCharactersToSync()

        try{
            toSync.first().map {
                val id = it.id.substring(it.id.indexOf("_") + 1, it.id.length)

                if(it.pendingDelete){
                    if(it.id.substring(0, 1) != "l") remote.deleteCharacter(it.id)
                    local.delete(it)
                }else if(it.id.substring(0, 1) == "l"){

                    var resposta : RemoteCharacter =
                        remote.createCharacter(it.toRemote())
                    local.delete(it)
                    local.insert(it.copy((resposta.id ?: "0"), pendingSync = false))

                }else{

                    remote.updateCharacter(it.toRemote())
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
            var characters = remote.getCharacters(campaignId)
            var ids = local.getIds()

            var charactersToUpdate : List<LocalCharacter> = ArrayList<LocalCharacter>()
            var charactersToInsert : List<LocalCharacter> = ArrayList<LocalCharacter>()

            characters.map {
                if(!(ids.first().contains(it.id))){
                    charactersToInsert = charactersToInsert.plus(it.toLocal())
                }else{
                    charactersToUpdate = charactersToUpdate.plus(it.toLocal())
                }
            }

            local.insertList(charactersToInsert)
            local.updateList(charactersToUpdate)
            return RepositoryResult.Success("Sicronizado con éxito")
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Se ha producido un error sincronizando del servidor.")
        }
    }

}