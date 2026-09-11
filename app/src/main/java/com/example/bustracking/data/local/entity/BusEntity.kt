package com.example.bustracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buses")
data class BusEntity(
    @PrimaryKey val busName: String,
    val driverName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val routeName: String = "Campus Route",
    val status: String = "Active", // Active, Idle, Maintenance
    val speedKmh: Float = 0f,
    val nextStop: String = "Main Gate",
    val etaMinutes: Int = 5,
    val lastUpdated: Long = System.currentTimeMillis()
)
