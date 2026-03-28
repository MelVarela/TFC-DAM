package com.example.notasmazmorras.data.model.local

import androidx.compose.ui.text.substring
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notasmazmorras.data.model.remote.RemoteCampaign

@Entity(tableName = "campaigns")
data class LocalCampaign(
    @PrimaryKey val id : String,
    val name : String,
    val picture : String,

    // Sincronización
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false
)

fun LocalCampaign.toRemote() : RemoteCampaign =
    RemoteCampaign(
        id = if (id.subSequence(0, 6) == "local_") null else id,
        name = name,
        picture = picture
    )