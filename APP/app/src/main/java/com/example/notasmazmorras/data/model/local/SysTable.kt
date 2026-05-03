package com.example.notasmazmorras.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "system_data")
data class SysTable(
    @PrimaryKey var id : String,
    var lastUser : String,
    var lastSinged : Long,
    var language : String,
    var authenticated : Boolean = false,
)
