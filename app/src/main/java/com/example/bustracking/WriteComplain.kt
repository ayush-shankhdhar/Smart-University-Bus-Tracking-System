package com.example.bustracking

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.data.local.AppDatabase
import com.example.bustracking.data.repository.ComplaintRepository
import com.example.bustracking.databinding.ActivityWriteComplainBinding
import com.google.firebase.auth.FirebaseAuth

class WriteComplain : AppCompatActivity() {
    private lateinit var binding: ActivityWriteComplainBinding
    private val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private val complaintRepository by lazy { ComplaintRepository(appDatabase.complaintDao()) }
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteComplainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Write Complaint"

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSubmit.setOnClickListener {
            val text = binding.etComplainBox.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "Enter your complaint message first", Toast.LENGTH_SHORT).show()
            } else {
                val email = firebaseAuth.currentUser?.email.toString()
                val author = email.split("@")[0]

                complaintRepository.submitComplaint(author, text) { success, error ->
                    if (success) {
                        Toast.makeText(this, "Your complaint has been submitted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to submit: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}