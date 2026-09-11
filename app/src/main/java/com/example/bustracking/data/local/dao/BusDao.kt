package com.example.bustracking.data.local.dao

import androidx.room.*
import com.example.bustracking.data.local.entity.BusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BusDao {
    @Query("SELECT * FROM buses ORDER BY busName ASC")
    fun getAllBusesFlow(): Flow<List<BusEntity>>

    @Query("SELECT * FROM buses ORDER BY busName ASC")
    suspend fun getAllBuses(): List<BusEntity>

    @Query("SELECT * FROM buses WHERE busName = :busName LIMIT 1")
    suspend fun getBusByName(busName: String): BusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBuses(buses: List<BusEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBus(bus: BusEntity)

    @Delete
    suspend fun deleteBus(bus: BusEntity)

    @Query("DELETE FROM buses")
    suspend fun deleteAll()
}
