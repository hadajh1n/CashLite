package com.example.cashlite.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.cashlite.R
import com.example.cashlite.Tab
import com.example.cashlite.databinding.ActivityMainBinding
import com.example.cashlite.viewModel.MainViewModel

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
        fragmentOpen()
        observeTabSelection()
    }

    private fun fragmentOpen() = with(binding) {
        val navController = findNavController(R.id.nav_host_fragment)
        llHistory.setOnClickListener {
            viewModel.selectTab(Tab.HISTORY)
            navigateIfNotCurrent(navController, R.id.historyFragment)
        }
        llGraphs.setOnClickListener {
            viewModel.selectTab(Tab.GRAPHS)
            navigateIfNotCurrent(navController, R.id.graphsFragment)
        }
        llSettings.setOnClickListener {
            viewModel.selectTab(Tab.SETTINGS)
            navigateIfNotCurrent(navController, R.id.settingsFragment)
        }
    }

    private fun navigateIfNotCurrent(navController: NavController, destinationId: Int) {
        if (navController.currentDestination?.id != destinationId) {
            navController.navigate(destinationId)
        }
    }

    private fun observeTabSelection() = with(binding) {
        viewModel.selectedTab.observe(this@MainActivity) { tab ->
            when (tab) {
                Tab.HISTORY -> {
                    highlightTab(tvHistory)
                    resetTab(tvGraphs)
                    resetTab(tvSettings)
                }
                Tab.GRAPHS -> {
                    resetTab(tvHistory)
                    highlightTab(tvGraphs)
                    resetTab(tvSettings)
                }
                Tab.SETTINGS -> {
                    resetTab(tvHistory)
                    resetTab(tvGraphs)
                    highlightTab(tvSettings)
                }
            }
        }
    }

    private fun highlightTab(tv: TextView) {
        tv.setTextColor(ContextCompat.getColor(this, R.color.black))
        tv.setBackgroundColor(ContextCompat.getColor(this, R.color.tvSelectedFragment))
    }

    private fun resetTab(tv: TextView) {
        tv.setTextColor(ContextCompat.getColor(this, R.color.white))
        tv.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
    }
}