package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_cards")
data class CustomCardEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val pickCount: Int = 1,
    val category: String = "CUSTOM",
    val isPunishment: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
