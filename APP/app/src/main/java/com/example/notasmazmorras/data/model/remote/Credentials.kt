package com.example.notasmazmorras.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    var email : String,
    var password : String
)
