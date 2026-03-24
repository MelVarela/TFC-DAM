package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.notasmazmorras.data.model.remote.RemotePlace

@Entity(
    tableName = "places",
    foreignKeys = [
        ForeignKey(
            entity = LocalCampaign::class,
            parentColumns = ["id"],
            childColumns = ["campaign"]
        )
    ],
    indices = [Index(value = ["campaign"])]
)
data class LocalPlace(
    @PrimaryKey val id : String,
    val name : String,
    val picture : String,

    val campaign : String,

    // Sincronización
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false
)

fun LocalPlace.toRemote() : RemotePlace =
    RemotePlace(
        id = if (id.subSequence(0, 6) == "local_") null else id,
        name = name,
        picture = picture,
        campaign = campaign
    )