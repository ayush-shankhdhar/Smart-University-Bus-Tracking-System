package com.example.bustracking

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bustracking.databinding.ActivityHomeBinding
import com.example.bustracking.service.LocationForegroundService
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var mAuth: FirebaseAuth
    private var isSharingLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Driver Dashboard"

        mAuth = FirebaseAuth.getInstance()
        val email = mAuth.currentUser?.email ?: "Driver"
        val driverName = email.split('@')[0]
        binding.tvAssignedBus.text = "Driver: $driverName | Route: Campus Circular"

        binding.btnShareLocation.setOnClickListener {
            if (!isSharingLocation) {
                checkAndStartTracking()
            } else {
                stopLocationService()
            }
        }
    }

    private fun checkAndStartTracking() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), LOCATION_PERMISSION_REQ_CODE)
        } else {
            startLocationService()
        }
    }

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        isSharingLocation = true
        binding.tvLocationStatus.text = "Status: Live Tracking Active"
        binding.btnShareLocation.text = "Stop Trip"
        Toast.makeText(this, "Location sharing service started", Toast.LENGTH_SHORT).show()
    }

    private fun stopLocationService() {
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        stopService(serviceIntent)

        isSharingLocation = false
        binding.tvLocationStatus.text = "Status: Location Tracking Idle"
        binding.btnShareLocation.text = "Start Trip"
        Toast.makeText(this, "Location sharing stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startLocationService()
            } else {
                Toast.makeText(this, "Location permissions are required to start trip tracking", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mLogout) {
            if (isSharingLocation) {
                stopLocationService()
            }
            mAuth.signOut()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQ_CODE = 2001
    }
}