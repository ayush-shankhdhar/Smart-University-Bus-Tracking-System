package com.example.bustracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey val busName: String,
    val routeName: String,
    val driverName: String = "",
    val addedAt: Long = System.currentTimeMillis()
)
