package com.example.bustracking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.adapters.ComplaintListAdapter
import com.example.bustracking.data.local.AppDatabase
import com.example.bustracking.data.repository.ComplaintRepository
import com.example.bustracking.databinding.ActivityAdminComplaintBoxBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdminComplaintBox : AppCompatActivity() {
    private lateinit var binding: ActivityAdminComplaintBoxBinding
    private val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private val complaintRepository by lazy { ComplaintRepository(appDatabase.complaintDao()) }
    private val complaintAdapter by lazy { ComplaintListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminComplaintBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Admin Complaint Box"

        binding.rvAdminComplain.apply {
            layoutManager = LinearLayoutManager(this@AdminComplaintBox)
            adapter = complaintAdapter
        }

        lifecycleScope.launch {
            complaintRepository.allComplaintsFlow.collectLatest { complaints ->
                complaintAdapter.submitList(complaints)
            }
        }

        complaintRepository.syncComplaintsFromFirebase()
    }
}