package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalCreature
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCreature(
    val id : String?,
    val name : String,
    val species : String,
    val picture : String = "",

    val campaign : String,
)

fun RemoteCreature.toLocal() : LocalCreature =
    LocalCreature(
        id = id!!,
        name = name,
        species = species,
        picture = picture,
        campaign = campaign
    )