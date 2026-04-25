package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notasmazmorras.data.model.remote.RemoteNote

@Entity(tableName = "notes")
data class LocalNote(
    @PrimaryKey val id : String,
    val name : String,
    val content : String,
    val isDm : Boolean,
    val isEditing : Boolean,

    val owner : String,

    // Sincronización
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false
)

fun LocalNote.toRemote() : RemoteNote =
    RemoteNote(
        id = if (id.subSequence(0, 6) == "local_") null else id,
        name = name,
        content = content,
        isDm = isDm,
        isEditing = isEditing,
        owner = owner
    )