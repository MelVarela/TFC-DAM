package com.example.notasmazmorras.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class Suggestion(
    val id: Int,
    val content: String,
    val type: String
)
