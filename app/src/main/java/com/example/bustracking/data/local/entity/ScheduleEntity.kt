package com.example.bustracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey val scheduleId: String,
    val busName: String,
    val routeName: String,
    val departureTime: String,
    val arrivalTime: String,
    val frequency: String,
    val rawText: String,
    val updatedAt: Long = System.currentTimeMillis()
)
