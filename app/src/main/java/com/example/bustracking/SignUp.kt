package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivitySignUpBinding
import com.example.bustracking.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private val networkUtils by lazy { NetworkUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.tvLoginInstead.setOnClickListener {
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!networkUtils.isOnline()) {
                Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    navigateToDashboard(email)
                } else {
                    val message = task.exception?.localizedMessage ?: "Registration failed"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToDashboard(email: String) {
        val domain = email.substringAfter('@', "")
        val targetActivity = when (domain) {
            "bus.com" -> Home::class.java       // Driver
            "admin.com" -> Admin::class.java   // Admin
            else -> StudentHome::class.java    // Student
        }
        startActivity(Intent(this, targetActivity))
        finish()
    }
}