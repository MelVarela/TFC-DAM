package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import com.example.notasmazmorras.data.model.remote.RemoteInventory

@Entity(
    tableName = "inventories",
    foreignKeys = [
        ForeignKey(
            entity = LocalCharacter::class,
            parentColumns = ["id"],
            childColumns = ["character"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = LocalObject::class,
            parentColumns = ["id"],
            childColumns = ["obxecto"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ],
    indices = [
        Index(value = ["character"]),
        Index(value = ["obxecto"])
              ],
    primaryKeys = [
        "character",
        "obxecto"
    ]
)
data class LocalInventory(
    val character: String,
    val obxecto: String,

    // Sincronización
    val existsRemote: Boolean = false,
    val pendingSync: Boolean = false,
    val pendingDelete: Boolean = false
)

fun LocalInventory.toRemote() : RemoteInventory =
    RemoteInventory(
        character = character,
        obxecto = obxecto
    )