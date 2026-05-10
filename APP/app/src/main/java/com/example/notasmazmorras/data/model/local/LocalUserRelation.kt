package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.notasmazmorras.data.model.remote.RemoteUserRelation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

@Entity(
    tableName = "user_relations",
    foreignKeys = [
        ForeignKey(
            entity = LocalCampaign::class,
            parentColumns = ["id"],
            childColumns = ["campaign"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ],
    indices = [
        Index(value = ["user"]),
        Index(value = ["campaign"])
    ],
    primaryKeys = [
        "user",
        "campaign"
    ]
)
data class LocalUserRelation(
    val isAccepted : Boolean,
    val role : String, //D -> Dm, P -> Player
    val schedule : String,

    val user : String,
    val campaign : String,

    // Sincronización
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false,
    val existsRemote: Boolean = false
)

fun LocalUserRelation.toRemote() : RemoteUserRelation =
    RemoteUserRelation(
        accepted = isAccepted,
        role = role,
        schedule = schedule,
        user = user,
        campaign = campaign
    )
