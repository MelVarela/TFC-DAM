package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.SysTable
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface SysTableDao {

    @Insert
    suspend fun insert(data : SysTable)

    @Update
    suspend fun update(data : SysTable)

    @Query("SELECT lastUser FROM system_data")
    fun getLastUser(): Flow<String>

    @Query("SELECT language FROM system_data")
    fun getLanguage(): Flow<String>

    @Query("SELECT lastSinged FROM system_data")
    fun getLastSinged(): Flow<Long>

    @Query("SELECT * FROM system_data")
    fun getSysData(): Flow<SysTable>

    @Query("SELECT COUNT(*) FROM system_data")
    fun getIsDataPresent(): Flow<Int>

}