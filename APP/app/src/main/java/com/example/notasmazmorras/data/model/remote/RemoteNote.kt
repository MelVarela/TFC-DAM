package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalNote
import kotlinx.serialization.Serializable

@Serializable
data class RemoteNote(
    val id : String?,
    val name : String,
    val content : String,
    val isDm : Boolean,
    val isEditing : Boolean,

    val owner : String,
)

fun RemoteNote.toLocal() : LocalNote =
    LocalNote(
        id = id!!,
        name = name,
        content = content,
        isDm = isDm,
        isEditing = isEditing,
        owner = owner
    )