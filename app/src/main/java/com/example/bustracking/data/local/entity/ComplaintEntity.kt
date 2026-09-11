package com.example.bustracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String,
    val complain: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Submitted" // Submitted, In Review, Resolved
)
