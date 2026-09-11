package com.example.bustracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey val routeId: String,
    val routeName: String,
    val startLocation: String,
    val endLocation: String,
    val totalDistanceKm: Double,
    val estimatedDurationMins: Int,
    val activeBusesCount: Int = 1
)
