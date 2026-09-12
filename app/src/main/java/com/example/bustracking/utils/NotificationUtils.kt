package com.example.bustracking.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationUtils {
    const val CHANNEL_ID_LOCATION = "campusride_location_channel"
    const val CHANNEL_NAME_LOCATION = "Bus Location Tracking"

    const val CHANNEL_ID_ALERTS = "campusride_alerts_channel"
    const val CHANNEL_NAME_ALERTS = "Bus & Schedule Alerts"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val locationChannel = NotificationChannel(
                CHANNEL_ID_LOCATION,
                CHANNEL_NAME_LOCATION,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Used for continuous background location tracking while driver trip is active"
            }

            val alertsChannel = NotificationChannel(
                CHANNEL_ID_ALERTS,
                CHANNEL_NAME_ALERTS,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for bus approaching, schedule changes, and alerts"
            }

            notificationManager.createNotificationChannel(locationChannel)
            notificationManager.createNotificationChannel(alertsChannel)
        }
    }
}
