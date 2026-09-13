package com.example.bustracking.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.bustracking.Home
import com.example.bustracking.R
import com.example.bustracking.data.local.AppDatabase
import com.example.bustracking.data.local.entity.BusEntity
import com.example.bustracking.modals.RVBusDriverModal
import com.example.bustracking.utils.NotificationUtils
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LocationForegroundService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val databaseReference by lazy { firebaseDatabase.getReference("Locations") }
    private val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private val mAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate() {
        super.onCreate()
        NotificationUtils.createNotificationChannels(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation ?: return
                val email = mAuth.currentUser?.email ?: return
                val username = email.split('@')[0]

                // 1. Update Firebase Realtime Database (Realtime Sync)
                val modal = RVBusDriverModal(username, location.longitude, location.latitude)
                databaseReference.child(username).setValue(modal)

                // 2. Update Local Room Database (Offline Cache)
                serviceScope.launch {
                    val busEntity = BusEntity(
                        busName = username,
                        driverName = username,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        status = "Active In-Transit",
                        speedKmh = location.speed * 3.6f,
                        lastUpdated = System.currentTimeMillis()
                    )
                    appDatabase.busDao().insertOrUpdateBus(busEntity)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_SERVICE) {
            stopForegroundService()
            return START_NOT_STICKY
        }

        val notification = createServiceNotification()
        startForeground(NOTIFICATION_ID, notification)
        startLocationUpdates()

        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(2000L)
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createServiceNotification(): Notification {
        val notificationIntent = Intent(this, Home::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, LocationForegroundService::class.java).apply {
            action = ACTION_STOP_SERVICE
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NotificationUtils.CHANNEL_ID_LOCATION)
            .setContentTitle("CampusRide Location Tracking Active")
            .setContentText("Sharing live bus coordinates with campus students...")
            .setSmallIcon(R.drawable.bus)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.bus, "Stop Trip", stopPendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun stopForegroundService() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        stopForegroundService()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val NOTIFICATION_ID = 1001
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    }
}
