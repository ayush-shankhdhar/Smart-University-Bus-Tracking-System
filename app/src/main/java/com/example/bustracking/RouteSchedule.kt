package com.example.bustracking

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.adapters.ScheduleListAdapter
import com.example.bustracking.data.local.AppDatabase
import com.example.bustracking.data.repository.ScheduleRepository
import com.example.bustracking.databinding.ActivityRouteScheduleBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RouteSchedule : AppCompatActivity() {
    private lateinit var binding: ActivityRouteScheduleBinding
    private val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private val scheduleRepository by lazy { ScheduleRepository(appDatabase.scheduleDao()) }
    private val scheduleAdapter by lazy { ScheduleListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Route Schedule"

        binding.rvSchedules.apply {
            layoutManager = LinearLayoutManager(this@RouteSchedule)
            adapter = scheduleAdapter
        }

        lifecycleScope.launch {
            scheduleRepository.schedulesFlow.collectLatest { schedules ->
                if (schedules.isNotEmpty()) {
                    scheduleAdapter.submitList(schedules)
                    binding.rvSchedules.visibility = View.VISIBLE
                    binding.tvRawFallback.visibility = View.GONE
                } else {
                    binding.rvSchedules.visibility = View.GONE
                    binding.tvRawFallback.visibility = View.VISIBLE
                    binding.tvRawFallback.text = "No schedules currently posted."
                }
            }
        }

        scheduleRepository.syncScheduleFromFirebase()
    }
}