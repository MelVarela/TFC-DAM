package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalUser
import kotlinx.serialization.Serializable

@Serializable
data class RemoteUser(
    val email : String,
    val name : String,
    val profilePicture : String = ""
)

fun RemoteUser.toLocal() : LocalUser =
    LocalUser(
        email = email,
        name = name,
        profilePicture = profilePicture
    )