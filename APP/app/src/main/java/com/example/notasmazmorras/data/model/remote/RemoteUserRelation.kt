package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalUserRelation
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class RemoteUserRelation(
    val isAccepted : Boolean,
    val role : String,
    val schedule : List<Date>,

    val id : String,
    val user : String,
    val campaign : String
)

fun RemoteUserRelation.toLocal() : LocalUserRelation =
    LocalUserRelation(
        isAccepted = isAccepted,
        role = role,
        schedule = schedule,
        id = id,
        user = user,
        campaign = campaign
    )