package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalInventory
import kotlinx.serialization.Serializable

@Serializable
data class RemoteInventory(
    val character: String,
    val obxecto: String
)

fun RemoteInventory.toLocal() : LocalInventory =
    LocalInventory(
        character = character,
        obxecto = obxecto
    )