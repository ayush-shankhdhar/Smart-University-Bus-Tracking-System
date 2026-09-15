package com.example.bustracking

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bustracking.databinding.ActivityMapsBinding
import com.example.bustracking.utils.LocationUtils
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var databaseReference: DatabaseReference

    private var busMarker: Marker? = null
    private var studentLocation: LatLng? = null
    private lateinit var busName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        busName = intent.getStringExtra("busName") ?: "CampusBus"
        supportActionBar?.title = "Tracking Bus: $busName"
        binding.tvMapHeaderTitle.text = "Bus $busName Live Location"
        binding.tvBusDetailName.text = "Bus $busName (Active Route)"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        databaseReference = FirebaseDatabase.getInstance().getReference("Locations")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        fetchStudentLocation()
        drawCampusRouteAndStops()
        listenForBusLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun fetchStudentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    studentLocation = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(studentLocation!!)
                            .title("Your Current Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawCampusRouteAndStops() {
        val points = LocationUtils.getSampleCampusRoutePoints()

        // Draw Route Polyline
        val polylineOptions = PolylineOptions()
            .addAll(points)
            .width(10f)
            .color(Color.parseColor("#1E3A8A"))
            .geodesic(true)
        mMap.addPolyline(polylineOptions)

        // Draw Stop Markers
        val stopNames = listOf("Main Gate", "Academic Block", "Central Library", "Hostel Complex", "Sports Arena")
        points.forEachIndexed { index, point ->
            mMap.addMarker(
                MarkerOptions()
                    .position(point)
                    .title("Stop: ${stopNames.getOrElse(index) { "Bus Stop" }}")
                    .snippet("Scheduled Stop #${index + 1}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            )
        }
    }

    private fun listenForBusLocationUpdates() {
        databaseReference.child(busName).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lat = (snapshot.child("latitude").value as? Number)?.toDouble() ?: 31.2536
                val lng = (snapshot.child("longitude").value as? Number)?.toDouble() ?: 75.7037

                val busLatLng = LatLng(lat, lng)

                if (busMarker == null) {
                    busMarker = mMap.addMarker(
                        MarkerOptions()
                            .position(busLatLng)
                            .title("Bus: $busName")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    )
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLatLng, 15f))
                } else {
                    busMarker?.position = busLatLng
                }

                // Update Distance & ETA if student location is available
                if (studentLocation != null) {
                    val meters = LocationUtils.calculateDistanceMeters(
                        studentLocation!!.latitude, studentLocation!!.longitude,
                        busLatLng.latitude, busLatLng.longitude
                    )
                    val etaMins = LocationUtils.calculateEtaMinutes(meters)
                    binding.tvDistanceText.text = LocationUtils.formatDistance(meters)
                    binding.tvEtaText.text = "Est. Arrival: $etaMins mins"
                } else {
                    binding.tvDistanceText.text = "Live Tracking Active"
                    binding.tvEtaText.text = "Lat: %.4f, Lng: %.4f".format(lat, lng)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MapsActivity, "Failed to connect to location stream", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
