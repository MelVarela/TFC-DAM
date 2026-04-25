package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalObject
import kotlinx.serialization.Serializable

@Serializable
data class RemoteObject(
    val id : String?,
    val name : String,
    val cost : Float,
    val picture : String,

    val campaign : String,
)

fun RemoteObject.toLocal() : LocalObject =
    LocalObject(
        id = id!!,
        name = name,
        cost = cost,
        picture = picture,
        campaign = campaign
    )