package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivitySignInBinding
import com.example.bustracking.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth
    private val networkUtils by lazy { NetworkUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!networkUtils.isOnline()) {
                Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show()
                    navigateToDashboard(email)
                } else {
                    val message = task.exception?.localizedMessage ?: "Invalid credentials"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser?.email != null) {
            navigateToDashboard(currentUser.email!!)
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