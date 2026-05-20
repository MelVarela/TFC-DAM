package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.LocalCharacter
import com.example.notasmazmorras.data.model.local.LocalInventory
import com.example.notasmazmorras.data.model.local.LocalObject
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert
    suspend fun insert(character: LocalCharacter)

    @Insert
    suspend fun insertList(characters : List<LocalCharacter>)

    @Insert
    suspend fun insertItems(item: LocalInventory)

    @Insert
    suspend fun insertItemList(items: List<LocalInventory>)

    @Update
    suspend fun update(character: LocalCharacter)

    @Update
    suspend fun updateList(characters : List<LocalCharacter>)

    @Update
    suspend fun updateItems(item: LocalInventory)

    @Update fun updateItemsList(item: List<LocalInventory>)

    @Delete
    suspend fun delete(character: LocalCharacter)

    @Query("DELETE FROM inventories WHERE character = :characterId AND obxecto = :itemId")
    suspend fun deleteItemById(characterId : String, itemId : String)

    @Delete
    suspend fun deleteItem(item: LocalInventory)

    @Query("UPDATE characters SET id = :id WHERE id = :oldId")
    suspend fun updateLocal(id: String, oldId: String)

    @Query("DELETE FROM characters")
    suspend fun deleteAll()

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacter(id: Int): Flow<LocalCharacter>

    @Query("SELECT * FROM characters WHERE campaign = :id")
    fun getCharactersFromCampaign(id: String): Flow<List<LocalCharacter>>

    @Query("SELECT * FROM characters WHERE pendingDelete = 0")
    fun getAllCharacters(): Flow<List<LocalCharacter>>

    @Query("SELECT * FROM inventories")
    fun getAllItems(): Flow<List<LocalInventory>>

    @Query("SELECT * FROM characters WHERE pendingSync = 1")
    fun getCharactersToSync(): Flow<List<LocalCharacter>>

    @Query("SELECT * FROM characters WHERE pendingDelete = 1")
    fun getCharactersToDelete(): Flow<List<LocalCharacter>>

    @Query("SELECT id FROM characters")
    fun getIds(): Flow<List<String>>

    @Query("SELECT * FROM inventories WHERE pendingSync = 1")
    fun getItemsToSync(): Flow<List<LocalInventory>>

    @Query("SELECT * FROM inventories WHERE pendingDelete = 1")
    fun getItemsToDelete(): Flow<List<LocalInventory>>

    @Query("SELECT * FROM objects o INNER JOIN inventories i ON i.obxecto = o.id WHERE character = :char AND i.pendingDelete = 0 ")
    fun getObjectsOf(char: String): Flow<List<LocalObject>>

}