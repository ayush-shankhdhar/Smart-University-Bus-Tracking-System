package com.example.bustracking.data.local.dao

import androidx.room.*
import com.example.bustracking.data.local.entity.ComplaintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY timestamp DESC")
    fun getAllComplaintsFlow(): Flow<List<ComplaintEntity>>

    @Query("SELECT * FROM complaints WHERE author = :author ORDER BY timestamp DESC")
    fun getComplaintsByAuthorFlow(author: String): Flow<List<ComplaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaints(complaints: List<ComplaintEntity>)

    @Query("DELETE FROM complaints WHERE id = :id")
    suspend fun deleteComplaintById(id: Int): Int
}
