package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.notasmazmorras.data.model.remote.RemoteCharacter

@Entity(
    tableName = "characters",
    foreignKeys = [
        ForeignKey(
            entity = LocalCampaign::class,
            parentColumns = ["id"],
            childColumns = ["campaign"]
        )
    ],
    indices = [Index(value = ["campaign"])]
)
data class LocalCharacter(
    @PrimaryKey val id : String,
    val name : String,
    val clase : String,
    val subClase : String,
    val maxPg : Int,
    val pg : Int,
    val picture : String,

    val campaing : String,

    // Sincronización
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false
)

fun LocalCharacter.toRemote() : RemoteCharacter =
    RemoteCharacter(
        id = if (id.subSequence(0, 6) == "local_") null else id,
        name = name,
        clase = clase,
        subClase = subClase,
        maxPg = maxPg,
        pg = pg,
        picture = picture,
        campaing = campaing,
        objects = null
    )