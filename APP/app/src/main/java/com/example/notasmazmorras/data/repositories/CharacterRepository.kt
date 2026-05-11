package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.DndClass
import com.example.notasmazmorras.data.model.remote.RemoteCampaign
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CharacterDao
import com.example.notasmazmorras.data.repositories.daos.NoteDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface CharacterRepository {

    fun getAllCharacters(): Flow<List<LocalCharacter>>

    suspend fun insertCharacter(character: LocalCharacter): RepositoryResult

    suspend fun updateCharacter(character: LocalCharacter): RepositoryResult

    suspend fun deleteCharacter(character: LocalCharacter): RepositoryResult

    suspend fun getClases() : List<DndClass>

    suspend fun getSubclasesFor(clase: String) : List<String>

    // Sincronización

    suspend fun uploadPendingChanges(): RepositoryResult

    suspend fun syncFromServer(campaignId : String): RepositoryResult

    suspend fun reset()
}

class DefaultCharacterRepository(
    private val local : CharacterDao,
    private val notes : NoteDao,
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

    override suspend fun getClases(): List<DndClass> {
        return remote.getClases()
    }

    override suspend fun getSubclasesFor(clase: String): List<String> {
        return remote.getClasesFor(clase)
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        var toSync = local.getCharactersToSync()

        try{
            toSync.first().map { character ->
                val id = character.id.substring(character.id.indexOf("_") + 1, character.id.length)

                if(character.pendingDelete){
                    if(character.id.substring(0, 1) != "l") remote.deleteCharacter(character.id)
                    local.delete(character)
                }else if(character.id.substring(0, 1) == "l" && character.campaign.substring(0, 1) != "l"){

                    var resposta : RemoteCharacter =
                        remote.createCharacter(character.toRemote())

                    local.updateLocal((resposta.id ?: character.id), character.id)

                    val nots = notes.getByOwner(character.id).first()

                    nots.map { note ->
                        notes.update(
                            note.copy(
                                owner = (resposta.id ?: character.id)
                            )
                        )
                    }

                }else{

                    if(character.id.substring(0, 1) != "l"){
                        remote.updateCharacter(character.toRemote())
                        local.update(character.copy(pendingSync = false))
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
            val characters = remote.getCharacters(campaignId)
            val ids = local.getIds().first()
            var workedIds : List<String> = emptyList()

            var charactersToUpdate : List<LocalCharacter> = ArrayList<LocalCharacter>()
            var charactersToInsert : List<LocalCharacter> = ArrayList<LocalCharacter>()

            characters.map {
                if(!(ids.contains(it.id))){
                    charactersToInsert = charactersToInsert.plus(it.toLocal())
                    workedIds = workedIds.plus(it.id ?: "")
                }else{
                    charactersToUpdate = charactersToUpdate.plus(it.toLocal())
                    workedIds = workedIds.plus(it.id ?: "")
                }
            }
            ids.map {
                if(!workedIds.contains(it) && it.substring(0, 1) != "l") local.deleteById(it)
            }

            local.insertList(charactersToInsert)
            local.updateList(charactersToUpdate)
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