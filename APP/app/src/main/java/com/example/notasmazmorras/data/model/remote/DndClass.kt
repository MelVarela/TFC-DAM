package com.example.notasmazmorras.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class DndClass(
    val index: String,
    val name: String,
    val subClases: List<String>
)
