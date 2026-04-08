package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notasmazmorras.data.model.remote.RemoteUser

@Entity(tableName = "users")
data class LocalUser(
    @PrimaryKey val email : String,
    val name : String,
    val password: String = "",
    val profilePicture : String,

    // Sincronización
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false
)

fun LocalUser.toRemote() : RemoteUser =
    RemoteUser(
        email = email,
        name = name,
        profilePicture = profilePicture
    )