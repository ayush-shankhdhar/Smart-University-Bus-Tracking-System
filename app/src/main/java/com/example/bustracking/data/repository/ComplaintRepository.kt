package com.example.bustracking.data.repository

import com.example.bustracking.data.local.dao.ComplaintDao
import com.example.bustracking.data.local.entity.ComplaintEntity
import com.example.bustracking.modals.ComplainModal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ComplaintRepository(private val complaintDao: ComplaintDao) {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("Complains")
    private val scope = CoroutineScope(Dispatchers.IO)

    fun getComplaintsByAuthorFlow(author: String): Flow<List<ComplaintEntity>> {
        return complaintDao.getComplaintsByAuthorFlow(author)
    }

    val allComplaintsFlow: Flow<List<ComplaintEntity>> = complaintDao.getAllComplaintsFlow()

    fun syncComplaintsFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ComplaintEntity>()
                for (child in snapshot.children) {
                    val modal = child.getValue(ComplainModal::class.java)
                    if (modal != null && !modal.author.isNullOrEmpty()) {
                        list.add(
                            ComplaintEntity(
                                author = modal.author!!,
                                complain = modal.complain ?: "",
                                status = "Submitted"
                            )
                        )
                    }
                }
                scope.launch {
                    complaintDao.insertComplaints(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Ignore or log
            }
        })
    }

    fun submitComplaint(author: String, complainText: String, onResult: (Boolean, String?) -> Unit) {
        val modal = ComplainModal(author, complainText)
        databaseReference.push().setValue(modal)
            .addOnSuccessListener {
                scope.launch {
                    complaintDao.insertComplaint(
                        ComplaintEntity(
                            author = author,
                            complain = complainText,
                            status = "Submitted"
                        )
                    )
                }
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }
}
