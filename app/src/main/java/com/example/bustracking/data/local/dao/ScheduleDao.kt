package com.example.bustracking.data.local.dao

import androidx.room.*
import com.example.bustracking.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules ORDER BY scheduleId ASC")
    fun getSchedulesFlow(): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules ORDER BY scheduleId ASC")
    suspend fun getSchedules(): List<ScheduleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSchedule(schedule: ScheduleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntity>)

    @Query("DELETE FROM schedules")
    suspend fun deleteAll()
}
