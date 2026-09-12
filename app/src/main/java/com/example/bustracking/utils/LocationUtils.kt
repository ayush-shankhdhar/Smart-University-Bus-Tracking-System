package com.example.bustracking.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

object LocationUtils {

    /**
     * Calculate distance between two LatLng points in meters
     */
    fun calculateDistanceMeters(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0]
    }

    /**
     * Format distance nicely (e.g., "450 m" or "2.4 km")
     */
    fun formatDistance(meters: Float): String {
        return if (meters < 1000) {
            "${meters.toInt()} m"
        } else {
            String.format(Locale.getDefault(), "%.1f km", meters / 1000f)
        }
    }

    /**
     * Estimate arrival time in minutes assuming average bus speed of 25 km/h
     */
    fun calculateEtaMinutes(meters: Float, avgSpeedKmh: Double = 25.0): Int {
        val kilometers = meters / 1000.0
        val hours = kilometers / avgSpeedKmh
        val minutes = (hours * 60).toInt()
        return if (minutes < 1) 1 else minutes
    }

    /**
     * Sample route polyline coordinates for demo campus routes
     */
    fun getSampleCampusRoutePoints(): List<LatLng> {
        return listOf(
            LatLng(31.2536, 75.7037), // Main Gate Stop
            LatLng(31.2550, 75.7050), // Academic Block Stop
            LatLng(31.2572, 75.7075), // Library Stop
            LatLng(31.2590, 75.7100), // Hostel Complex Stop
            LatLng(31.2610, 75.7125)  // Sports Arena Stop
        )
    }
}
