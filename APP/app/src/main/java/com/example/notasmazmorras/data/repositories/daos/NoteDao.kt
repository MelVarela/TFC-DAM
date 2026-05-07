package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.LocalNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: LocalNote)

    @Insert
    suspend fun insertList(notes : List<LocalNote>)

    @Update
    suspend fun update(note: LocalNote)

    @Update
    suspend fun updateList(notes : List<LocalNote>)

    @Delete
    suspend fun delete(note: LocalNote)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Int): Flow<LocalNote>

    @Query("SELECT * FROM notes WHERE pendingDelete = 0")
    fun getAllNotes(): Flow<List<LocalNote>>

    @Query("SELECT * FROM notes WHERE pendingSync = 1")
    fun getNotesToSync(): Flow<List<LocalNote>>

    @Query("SELECT * FROM notes WHERE pendingDelete = 1")
    fun getNotesToDelete(): Flow<List<LocalNote>>

    @Query("SELECT id FROM notes")
    fun getIds(): Flow<List<String>>

}