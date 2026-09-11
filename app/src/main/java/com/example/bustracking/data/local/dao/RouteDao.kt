package com.example.bustracking.data.local.dao

import androidx.room.*
import com.example.bustracking.data.local.entity.RouteEntity
import com.example.bustracking.data.local.entity.StopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Query("SELECT * FROM routes ORDER BY routeName ASC")
    fun getAllRoutesFlow(): Flow<List<RouteEntity>>

    @Query("SELECT * FROM routes ORDER BY routeName ASC")
    suspend fun getAllRoutes(): List<RouteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<RouteEntity>)

    @Query("SELECT * FROM stops WHERE routeId = :routeId ORDER BY sequenceOrder ASC")
    suspend fun getStopsForRoute(routeId: String): List<StopEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStops(stops: List<StopEntity>)
}
