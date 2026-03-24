package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalCampaign
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCampaign(
    val id : String?,
    val name : String,
    val picture : String,

    val users : List<String>?,
)

fun RemoteCampaign.toLocal() : LocalCampaign =
    LocalCampaign(
        id = id!!,
        name = name,
        picture = picture
    )