package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.LocalCreature
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatureDao {

    @Insert
    suspend fun insert(creature: LocalCreature)

    @Insert
    suspend fun insertList(creatures : List<LocalCreature>)

    @Update
    suspend fun update(creature: LocalCreature)

    @Update
    suspend fun updateList(creatures : List<LocalCreature>)

    @Delete
    suspend fun delete(creature: LocalCreature)

    @Query("UPDATE creatures SET id = :id WHERE id = :oldId")
    suspend fun updateLocal(id: String, oldId: String)

    @Query("DELETE FROM creatures")
    suspend fun deleteAll()

    @Query("DELETE FROM creatures WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM creatures WHERE id = :id")
    fun getCreature(id: Int): Flow<LocalCreature>

    @Query("SELECT * FROM creatures WHERE campaign = :id")
    fun getCreaturesFromCampaign(id: String): Flow<List<LocalCreature>>

    @Query("SELECT * FROM creatures WHERE pendingDelete = 0")
    fun getAllCreatures(): Flow<List<LocalCreature>>

    @Query("SELECT * FROM creatures WHERE pendingSync = 1")
    fun getCreaturesToSync(): Flow<List<LocalCreature>>

    @Query("SELECT * FROM creatures WHERE pendingDelete = 1")
    fun getCreaturesToDelete(): Flow<List<LocalCreature>>

    @Query("SELECT id FROM creatures")
    fun getIds(): Flow<List<String>>

}