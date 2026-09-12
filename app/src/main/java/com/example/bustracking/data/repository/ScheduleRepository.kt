package com.example.bustracking.data.repository

import com.example.bustracking.data.local.dao.ScheduleDao
import com.example.bustracking.data.local.entity.ScheduleEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ScheduleRepository(private val scheduleDao: ScheduleDao) {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("RouteSchedule")
    private val scope = CoroutineScope(Dispatchers.IO)

    val schedulesFlow: Flow<List<ScheduleEntity>> = scheduleDao.getSchedulesFlow()

    fun syncScheduleFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rawVal = snapshot.value?.toString() ?: ""
                if (rawVal.isNotEmpty()) {
                    val scheduleEntity = ScheduleEntity(
                        scheduleId = "main_schedule",
                        busName = "Campus Shuttle",
                        routeName = "University Express",
                        departureTime = "08:00 AM",
                        arrivalTime = "06:00 PM",
                        frequency = "Every 30 Mins",
                        rawText = rawVal
                    )
                    scope.launch {
                        scheduleDao.insertOrUpdateSchedule(scheduleEntity)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle cancellation
            }
        })
    }

    fun updateScheduleInFirebase(newSchedule: String, onResult: (Boolean, String?) -> Unit) {
        databaseReference.setValue(newSchedule)
            .addOnSuccessListener {
                scope.launch {
                    scheduleDao.insertOrUpdateSchedule(
                        ScheduleEntity(
                            scheduleId = "main_schedule",
                            busName = "Campus Shuttle",
                            routeName = "University Express",
                            departureTime = "08:00 AM",
                            arrivalTime = "06:00 PM",
                            frequency = "Every 30 Mins",
                            rawText = newSchedule
                        )
                    )
                }
                onResult(true, null)
            }
            .addOnFailureListener { exception ->
                onResult(false, exception.message)
            }
    }
}
