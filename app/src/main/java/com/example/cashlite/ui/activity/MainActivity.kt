package com.example.cashlite.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cashlite.R
import com.example.cashlite.core.utils.Tab
import com.example.cashlite.databinding.ActivityMainBinding
import com.example.cashlite.ui.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.historyFragment -> viewModel.selectTab(Tab.HISTORY)
                R.id.graphsFragment -> viewModel.selectTab(Tab.GRAPHS)
                R.id.settingsFragment -> viewModel.selectTab(Tab.SETTINGS)
            }
        }
    }
}