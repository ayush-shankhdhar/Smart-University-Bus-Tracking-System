package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.adapters.ComplaintListAdapter
import com.example.bustracking.data.local.AppDatabase
import com.example.bustracking.data.repository.ComplaintRepository
import com.example.bustracking.databinding.ActivityComplaintBoxBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ComplaintBox : AppCompatActivity() {
    private lateinit var binding: ActivityComplaintBoxBinding
    private val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private val complaintRepository by lazy { ComplaintRepository(appDatabase.complaintDao()) }
    private val complaintAdapter by lazy { ComplaintListAdapter() }
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Complaint Box"

        firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()
        val username = email.split("@")[0]

        binding.btnWriteComplain.setOnClickListener {
            startActivity(Intent(this, WriteComplain::class.java))
        }

        binding.rvComplains.apply {
            layoutManager = LinearLayoutManager(this@ComplaintBox)
            adapter = complaintAdapter
        }

        lifecycleScope.launch {
            complaintRepository.getComplaintsByAuthorFlow(username).collectLatest { complaints ->
                complaintAdapter.submitList(complaints)
            }
        }

        complaintRepository.syncComplaintsFromFirebase()
    }
}