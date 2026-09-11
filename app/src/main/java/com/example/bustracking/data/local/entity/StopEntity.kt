package com.example.bustracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class StopEntity(
    @PrimaryKey val stopId: String,
    val stopName: String,
    val routeId: String,
    val sequenceOrder: Int,
    val latitude: Double,
    val longitude: Double,
    val scheduledTime: String
)
