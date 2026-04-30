package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalNote
import kotlinx.serialization.Serializable

@Serializable
data class RemoteNote(
    val id : String?,
    val name : String,
    val content : String,
    val dm : Boolean,
    val editing : Boolean,

    val owner : String,
)

fun RemoteNote.toLocal() : LocalNote =
    LocalNote(
        id = id!!,
        name = name,
        content = content,
        isDm = dm,
        isEditing = editing,
        owner = owner
    )