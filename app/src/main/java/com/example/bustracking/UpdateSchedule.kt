package com.example.bustracking

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.data.local.AppDatabase
import com.example.bustracking.data.repository.ScheduleRepository
import com.example.bustracking.databinding.ActivityUpdateScheduleBinding

class UpdateSchedule : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateScheduleBinding
    private val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private val scheduleRepository by lazy { ScheduleRepository(appDatabase.scheduleDao()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Update Schedule"

        scheduleRepository.syncScheduleFromFirebase()

        binding.btnUpdate.setOnClickListener {
            val text = binding.etRouteSchedule.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter schedule details first", Toast.LENGTH_SHORT).show()
            } else {
                scheduleRepository.updateScheduleInFirebase(text) { success, error ->
                    if (success) {
                        Toast.makeText(this, "Schedule updated successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}