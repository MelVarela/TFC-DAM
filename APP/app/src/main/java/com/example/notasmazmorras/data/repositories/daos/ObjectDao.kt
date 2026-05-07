package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.LocalObject
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjectDao {

    @Insert
    suspend fun insert(obxecto : LocalObject)

    @Insert
    suspend fun insertList(objects : List<LocalObject>)

    @Update
    suspend fun update(obxecto : LocalObject)

    @Update
    suspend fun updateList(objects : List<LocalObject>)

    @Delete
    suspend fun delete(obxecto : LocalObject)

    @Query("DELETE FROM objects")
    suspend fun deleteAll()

    @Query("SELECT * FROM objects WHERE id = :id")
    fun getObject(id: Int): Flow<LocalObject>

    @Query("SELECT * FROM objects WHERE pendingDelete = 0")
    fun getAllObjects(): Flow<List<LocalObject>>

    @Query("SELECT * FROM objects WHERE pendingSync = 1")
    fun getObjectsToSync(): Flow<List<LocalObject>>

    @Query("SELECT * FROM objects WHERE pendingDelete = 1")
    fun getObjectsToDelete(): Flow<List<LocalObject>>

    @Query("SELECT id FROM objects")
    fun getIds(): Flow<List<String>>

}