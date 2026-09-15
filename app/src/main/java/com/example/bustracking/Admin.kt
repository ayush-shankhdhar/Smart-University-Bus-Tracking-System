package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.adapters.BusListAdapter
import com.example.bustracking.data.local.AppDatabase
import com.example.bustracking.data.repository.BusRepository
import com.example.bustracking.databinding.ActivityAdminBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Admin : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var mAuth: FirebaseAuth

    private val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private val busRepository by lazy { BusRepository(appDatabase.busDao()) }
    private lateinit var busListAdapter: BusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "CampusRide Admin Dashboard"

        mAuth = FirebaseAuth.getInstance()

        setupRecyclerView()
        setupListeners()
        observeBuses()

        busRepository.syncBusesFromFirebase()
    }

    private fun setupRecyclerView() {
        busListAdapter = BusListAdapter(
            onBusClick = { bus ->
                MaterialAlertDialogBuilder(this)
                    .setTitle("Manage Bus: ${bus.busName}")
                    .setMessage("Driver: ${bus.driverName}\nStatus: ${bus.status}\nCoordinates: (${bus.latitude}, ${bus.longitude})")
                    .setPositiveButton("View on Map") { _, _ ->
                        val intent = Intent(this, MapsActivity::class.java)
                        intent.putExtra("busName", bus.busName)
                        startActivity(intent)
                    }
                    .setNegativeButton("Close", null)
                    .show()
            },
            onFavToggle = { bus ->
                Toast.makeText(this, "Admin view: ${bus.busName}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@Admin)
            adapter = busListAdapter
        }
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            busRepository.syncBusesFromFirebase()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.btnUpdateSchedule.setOnClickListener {
            startActivity(Intent(this, UpdateSchedule::class.java))
        }

        binding.btnShowComplaintBox.setOnClickListener {
            startActivity(Intent(this, AdminComplaintBox::class.java))
        }
    }

    private fun observeBuses() {
        lifecycleScope.launch {
            busRepository.allBusesFlow.collectLatest { buses ->
                binding.progressBar.visibility = View.GONE
                busListAdapter.submitList(buses)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mLogout) {
            Toast.makeText(this, "Admin Logged Out", Toast.LENGTH_SHORT).show()
            mAuth.signOut()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}