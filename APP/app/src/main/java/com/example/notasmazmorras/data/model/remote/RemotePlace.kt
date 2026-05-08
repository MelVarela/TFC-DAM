package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalPlace
import kotlinx.serialization.Serializable

@Serializable
data class RemotePlace(
    val id : String?,
    val name : String,
    val picture : String,

    val campaign : String,
)

fun RemotePlace.toLocal() : LocalPlace =
    LocalPlace(
        id = id!!,
        name = name,
        picture = picture,
        campaign = campaign
    )