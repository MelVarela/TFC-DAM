package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.notasmazmorras.data.model.remote.RemoteUserRelation
import java.util.Date

@Entity(
    tableName = "user_relations",
    foreignKeys = [
        ForeignKey(
            entity = LocalUser::class,
            parentColumns = ["id"],
            childColumns = ["user"]
        ),
        ForeignKey(
            entity = LocalCampaign::class,
            parentColumns = ["id"],
            childColumns = ["campaign"]
        )
    ],
    indices = [
        Index(value = ["user"]),
        Index(value = ["campaign"])
    ]
)
data class LocalUserRelation(
    val isAccepted : Boolean,
    val role : String,
    val schedule : List<Date>,

    @PrimaryKey val user : String,
    @PrimaryKey val campaign : String,

    // Sincronización
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false
)

fun LocalUserRelation.toRemote() : RemoteUserRelation =
    RemoteUserRelation(
        isAccepted = isAccepted,
        role = role,
        schedule = schedule,
        user = user,
        campaign = campaign
    )
