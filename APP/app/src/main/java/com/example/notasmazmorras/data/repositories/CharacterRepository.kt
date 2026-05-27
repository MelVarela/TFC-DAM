package com.example.notasmazmorras.data.repositories

import android.util.Log
import com.example.notasmazmorras.data.model.local.LocalCampaign
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.model.local.LocalInventory
import com.example.notasmazmorras.data.model.local.LocalObject
import com.example.notasmazmorras.data.model.local.toRemote
import com.example.notasmazmorras.data.model.remote.DndClass
import com.example.notasmazmorras.data.model.remote.RemoteCampaign
import com.example.notasmazmorras.data.model.remote.RemoteCharacter
import com.example.notasmazmorras.data.model.remote.RemoteInventory
import com.example.notasmazmorras.data.model.remote.toLocal
import com.example.notasmazmorras.data.repositories.daos.CharacterDao
import com.example.notasmazmorras.data.repositories.daos.NoteDao
import com.example.notasmazmorras.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

interface CharacterRepository {

    fun getAllCharacters(): Flow<List<LocalCharacter>>

    fun getAllInventories(): Flow<List<LocalInventory>>

    suspend fun insertCharacter(character: LocalCharacter): RepositoryResult

    suspend fun updateCharacter(character: LocalCharacter): RepositoryResult

    suspend fun deleteCharacter(character: LocalCharacter): RepositoryResult

    suspend fun getClases() : List<DndClass>

    suspend fun getSubclasesFor(clase: String) : List<String>

    suspend fun addItem(item: LocalInventory) : RepositoryResult

    suspend fun updateItem(item: LocalInventory) : RepositoryResult

    suspend fun isPending(char: String, obj: String) : Boolean

    suspend fun deleteItem(item: LocalInventory) : RepositoryResult

    suspend fun getItemsOf(char: String) : List<LocalObject>

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

    val TAG = "character_repository"
    val NO_ERR = "No se proporcionó mensaje de error."

    override fun getAllCharacters(): Flow<List<LocalCharacter>> = local.getAllCharacters()

    override fun getAllInventories(): Flow<List<LocalInventory>> = local.getAllItems()

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

    override suspend fun addItem(item: LocalInventory): RepositoryResult {
        try{
            local.insertItems(item.copy(pendingSync = true))
            return RepositoryResult.Success("Objeto '${item.obxecto}' dado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error dando el objeto '${item.obxecto}'.")
        }
    }

    override suspend fun updateItem(item: LocalInventory): RepositoryResult {
        try{
            local.updateItems(item.copy(pendingSync = true))
            return RepositoryResult.Success("Relación con el objeto '${item.obxecto}' actualizado con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            if(item.pendingDelete) return RepositoryResult.Error("Error eliminando la relacion con el objeto '${item.obxecto}'.")
            else return RepositoryResult.Error("Error actualizando la relacion con elobjeto '${item.obxecto}'.")
        }
    }

    override suspend fun isPending(char: String, obj: String): Boolean {
        try{
            return local.isPending(char, obj).first()
        }catch (e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return false
        }
    }

    override suspend fun deleteItem(item: LocalInventory): RepositoryResult {
        try{
            updateItem(item.copy(pendingSync = true, pendingDelete = true))
            return RepositoryResult.Success("Relación con el objeto '${item.obxecto}' eliminada con éxito.")
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            return RepositoryResult.Error("Error eliminando la relacion con el objeto '${item.obxecto}'.")
        }
    }

    override suspend fun getItemsOf(char: String): List<LocalObject> {
        try{
            return local.getObjectsOf(char).first()
        }catch(e : Throwable){
            Log.e(TAG, e.message ?: NO_ERR)
            e.printStackTrace()
            return emptyList()
        }
    }

    override suspend fun uploadPendingChanges(): RepositoryResult {
        var toSync = local.getCharactersToSync()
        var toSyncItems = local.getItemsToSync()

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

            toSyncItems.first().map { item ->
                val charId = item.character
                val objId = item.obxecto

                if(item.pendingDelete){
                    if(item.existsRemote) remote.deleteInventory("${item.character}.${item.obxecto}")
                    local.deleteItem(item)
                }else if(!item.existsRemote || item.pendingSync){

                    var resposta : RemoteInventory =
                        remote.createInventory(item.toRemote())

                    local.updateItems(resposta.toLocal().copy(existsRemote = true))

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

            characters.map { char ->
                if(!(ids.contains(char.id))){
                    charactersToInsert = charactersToInsert.plus(char.toLocal())
                    workedIds = workedIds.plus(char.id ?: "")
                }else{
                    charactersToUpdate = charactersToUpdate.plus(char.toLocal())
                    workedIds = workedIds.plus(char.id ?: "")
                }

                if(char.id != null){
                    val items = remote.getItemsOf(char.id)
                    val objectIds : List<String> = local.getAllItems().first().filter { it.character == char.id }.map { it.obxecto }
                    var workedItems : List<String> = emptyList()

                    var itemsToInsert : List<LocalInventory> = ArrayList<LocalInventory>()

                    items.map {
                        if(!(objectIds.contains(it.obxecto))){
                            itemsToInsert = itemsToInsert.plus(it.toLocal())
                            workedItems = workedItems.plus(it.obxecto)
                        }
                    }

                    objectIds.map {
                        if(!workedItems.contains(it)) local.deleteItemById(char.id, it)
                    }
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