package com.example.bustracking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bustracking.adapters.BusListAdapter
import com.example.bustracking.data.local.AppDatabase
import com.example.bustracking.data.repository.BusRepository
import com.example.bustracking.data.repository.FavouriteRepository
import com.example.bustracking.databinding.ActivityStudentHomeBinding
import com.example.bustracking.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StudentHome : AppCompatActivity() {

    private lateinit var binding: ActivityStudentHomeBinding
    private lateinit var mAuth: FirebaseAuth

    private val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private val busRepository by lazy { BusRepository(appDatabase.busDao()) }
    private val favouriteRepository by lazy { FavouriteRepository(appDatabase.favouriteDao()) }
    private val networkUtils by lazy { NetworkUtils(this) }

    private lateinit var busListAdapter: BusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "CampusRide Student Dashboard"

        mAuth = FirebaseAuth.getInstance()

        setupRecyclerView()
        setupListeners()
        observeNetworkConnectivity()
        observeBuses()

        // Sync latest buses from Firebase
        busRepository.syncBusesFromFirebase()
    }

    private fun setupRecyclerView() {
        busListAdapter = BusListAdapter(
            onBusClick = { bus ->
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("busName", bus.busName)
                startActivity(intent)
            },
            onFavToggle = { bus ->
                lifecycleScope.launch {
                    val added = favouriteRepository.toggleFavourite(
                        bus.busName,
                        bus.routeName,
                        bus.driverName
                    )
                    val msg = if (added) "Saved to Favourites!" else "Removed from Favourites"
                    Toast.makeText(this@StudentHome, msg, Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.rvBusDrivers.apply {
            layoutManager = LinearLayoutManager(this@StudentHome)
            adapter = busListAdapter
        }
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            busRepository.syncBusesFromFirebase()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.btnSchedule.setOnClickListener {
            startActivity(Intent(this, RouteSchedule::class.java))
        }

        binding.btnChatBox.setOnClickListener {
            startActivity(Intent(this, ChatBox::class.java))
        }

        binding.btnComplainBox.setOnClickListener {
            startActivity(Intent(this, ComplaintBox::class.java))
        }
    }

    private fun observeNetworkConnectivity() {
        lifecycleScope.launch {
            networkUtils.observeConnectivity.collectLatest { isOnline ->
                binding.layoutOfflineBanner.visibility = if (isOnline) View.GONE else View.VISIBLE
            }
        }
    }

    private fun observeBuses() {
        lifecycleScope.launch {
            busRepository.allBusesFlow.collectLatest { buses ->
                binding.progressBar.visibility = View.GONE
                busListAdapter.submitList(buses)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_screen_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mLogout) {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            mAuth.signOut()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}