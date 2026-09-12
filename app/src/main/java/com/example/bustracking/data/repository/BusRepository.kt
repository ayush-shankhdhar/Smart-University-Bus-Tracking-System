package com.example.bustracking.data.repository

import com.example.bustracking.data.local.dao.BusDao
import com.example.bustracking.data.local.entity.BusEntity
import com.example.bustracking.modals.RVBusDriverModal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BusRepository(private val busDao: BusDao) {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("Locations")
    private val scope = CoroutineScope(Dispatchers.IO)

    val allBusesFlow: Flow<List<BusEntity>> = busDao.getAllBusesFlow()

    fun syncBusesFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val busList = mutableListOf<BusEntity>()
                for (child in snapshot.children) {
                    val modal = child.getValue(RVBusDriverModal::class.java)
                    if (modal?.userName != null) {
                        val busEntity = BusEntity(
                            busName = modal.userName!!,
                            driverName = modal.userName!!,
                            latitude = modal.latitude ?: 0.0,
                            longitude = modal.longitude ?: 0.0,
                            routeName = "Campus Route ${modal.userName}",
                            status = "Active",
                            lastUpdated = System.currentTimeMillis()
                        )
                        busList.add(busEntity)
                    }
                }
                scope.launch {
                    busDao.insertOrUpdateBuses(busList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log or handle error gracefully
            }
        })
    }

    suspend fun getBusByName(name: String): BusEntity? {
        return busDao.getBusByName(name)
    }
}
