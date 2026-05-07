package com.example.notasmazmorras.data.model

import com.example.notasmazmorras.data.model.local.LocalUser
import com.example.notasmazmorras.data.model.remote.RemoteUser
import kotlinx.serialization.Serializable

@Serializable
data class UserAccount(
    val email : String,
    val password: String,
    val name : String,
    val profilePicture : String,
)

fun UserAccount.toLocal() : LocalUser = LocalUser(
    email = email,
    name = name,
    profilePicture = profilePicture
)