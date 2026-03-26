package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.LocalUserRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRelationDao {

    @Insert
    suspend fun insert(userRelation: LocalUserRelation)

    @Insert
    suspend fun insertList(relations : List<LocalUserRelation>)

    @Update
    suspend fun update(userRelation: LocalUserRelation)

    @Update
    suspend fun updateList(relations : List<LocalUserRelation>)

    @Delete
    suspend fun delete(userRelation: LocalUserRelation)

    @Query("SELECT * FROM user_relations WHERE user = :user and campaign = :campaign")
    fun getUserRelation(user: String, campaign: String): Flow<LocalUserRelation>

    @Query("SELECT * FROM user_relations WHERE pendingDelete = 0")
    fun getAllRelations(): Flow<List<LocalUserRelation>>

    @Query("SELECT * FROM user_relations WHERE pendingSync = 1")
    fun getRelationsToSync(): Flow<List<LocalUserRelation>>

    @Query("SELECT * FROM user_relations WHERE pendingDelete = 1")
    fun getRelationsToDelete(): Flow<List<LocalUserRelation>>

}