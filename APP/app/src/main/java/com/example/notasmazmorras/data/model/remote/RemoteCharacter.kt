package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalCharacter
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    val id : String?,
    val name : String,
    val clase : String,
    val subClase : String,
    val maxPg : Int,
    val pg : Int,
    val picture : String,

    val campaing : String,
    val objects : List<String>?,
)

fun RemoteCharacter.toLocal() : LocalCharacter =
    LocalCharacter(
        id = id!!,
        name = name,
        clase = clase,
        subClase = subClase,
        maxPg = maxPg,
        pg = pg,
        picture = picture,
        campaing = campaing
    )