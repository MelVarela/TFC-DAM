package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.LocalCampaign
import kotlinx.coroutines.flow.Flow

@Dao
interface CampaignDao {

    @Insert
    suspend fun insert(campaign : LocalCampaign)

    @Insert
    suspend fun insertList(campaigns : List<LocalCampaign>)

    @Update
    suspend fun update(campaign: LocalCampaign)

    @Update
    suspend fun updateList(campaigns: List<LocalCampaign>)

    @Delete
    suspend fun delete(campaign: LocalCampaign)

    @Query("DELETE FROM campaigns")
    suspend fun deleteAll()

    @Query("SELECT * FROM campaigns WHERE id = :id")
    fun getCampaign(id: Int): Flow<LocalCampaign>

    @Query("SELECT * FROM campaigns WHERE pendingDelete = 0")
    fun getAllCampaigns(): Flow<List<LocalCampaign>>

    @Query("SELECT * FROM campaigns WHERE pendingSync = 1")
    fun getCampaignsToSync(): Flow<List<LocalCampaign>>

    @Query("SELECT * FROM campaigns WHERE pendingDelete = 1")
    fun getCampaignsToDelete(): Flow<List<LocalCampaign>>

    @Query("SELECT id FROM campaigns")
    fun getIds(): Flow<List<String>>

}