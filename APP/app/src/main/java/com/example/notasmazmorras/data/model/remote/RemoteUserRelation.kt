package com.example.notasmazmorras.data.model.remote

import com.example.notasmazmorras.data.model.local.LocalUserRelation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

@Serializable
data class RemoteUserRelation(
    val isAccepted : Boolean,
    val role : String,
    val schedule : List<Long>,

    val id : String,
    val user : String,
    val campaign : String
)

fun RemoteUserRelation.toLocal() : LocalUserRelation =
    LocalUserRelation(
        isAccepted = isAccepted,
        role = role,
        schedule = listLongToDate(schedule),
        id = id,
        user = user,
        campaign = campaign
    )

private fun listLongToDate(list : List<Long>) : List<LocalDateTime> {
    var dev : ArrayList<LocalDateTime> = ArrayList<LocalDateTime>()
    list.forEach { dev.add(LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC)) }
    return dev.toList()
}