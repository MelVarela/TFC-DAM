package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.notasmazmorras.data.model.remote.RemoteObject

@Entity(
    tableName = "objects",
    foreignKeys = [
        ForeignKey(
            entity = LocalCampaign::class,
            parentColumns = ["id"],
            childColumns = ["campaign"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ],
    indices = [Index(value = ["campaign"])]
)
data class LocalObject(
    @PrimaryKey val id : String,
    val name : String,
    val cost : Float,
    val picture : String,

    val campaign : String,

    // Sincronización
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false
)

fun LocalObject.toRemote() : RemoteObject =
    RemoteObject(
        id = if (id.subSequence(0, 6) == "local_") null else id,
        name = name,
        cost = cost,
        picture = picture,
        campaign = campaign
    )