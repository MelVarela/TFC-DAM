package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.LocalPlace
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Insert
    suspend fun insert(place : LocalPlace)

    @Insert
    suspend fun insertList(places : List<LocalPlace>)

    @Update
    suspend fun update(place : LocalPlace)

    @Update
    suspend fun updateList(places : List<LocalPlace>)

    @Delete
    suspend fun delete(place : LocalPlace)

    @Query("SELECT * FROM places WHERE id = :id")
    fun getPlace(id: Int): Flow<LocalPlace>

    @Query("SELECT * FROM places WHERE pendingDelete = 0")
    fun getAllPlaces(): Flow<List<LocalPlace>>

    @Query("SELECT * FROM places WHERE pendingSync = 1")
    fun getPlacesToSync(): Flow<List<LocalPlace>>

    @Query("SELECT * FROM places WHERE pendingDelete = 1")
    fun getPlacesToDelete(): Flow<List<LocalPlace>>

    @Query("SELECT id FROM places")
    fun getIds(): Flow<List<String>>

}