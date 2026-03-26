package com.example.notasmazmorras.data.repositories.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notasmazmorras.data.model.local.LocalUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user : LocalUser)

    @Insert
    suspend fun insertList(users : List<LocalUser>)

    @Update
    suspend fun update(user : LocalUser)

    @Update
    suspend fun updateList(users : List<LocalUser>)

    @Delete
    suspend fun delete(user : LocalUser)

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUser(email: String): Flow<LocalUser>

    @Query("SELECT * FROM users WHERE pendingDelete = 0")
    fun getAllUsers(): Flow<List<LocalUser>>

    @Query("SELECT * FROM users WHERE pendingSync = 1")
    fun getUsersToSync(): Flow<List<LocalUser>>

    @Query("SELECT * FROM users WHERE pendingDelete = 1")
    fun getUsersToDelete(): Flow<List<LocalUser>>

    @Query("SELECT email FROM users")
    fun getUserEmails(): Flow<List<String>>

}