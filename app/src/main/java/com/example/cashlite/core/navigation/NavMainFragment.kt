package com.example.cashlite.core.navigation

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentMainBinding
import com.example.cashlite.ui.activity.AddNewOperationActivity

fun Fragment.setupBottomNav(binding: FragmentMainBinding) = with(binding) {

    val navHostFragment = childFragmentManager
        .findFragmentById(R.id.nav_host_main_set_fragment) as? NavHostFragment
        ?: return@with

    val navController = navHostFragment.navController

    fun selectButton(selected: View) {
        btnFragmentHistory.isSelected = false
        btnFragmentGraphs.isSelected = false
        btnFragmentReport.isSelected = false
        btnFragmentSettings.isSelected = false
        selected.isSelected = true
    }

    fun navigateSafe(destinationId: Int) {
        if (navController.currentDestination?.id == destinationId) return

        navController.navigate(destinationId, null, navOptions {
            launchSingleTop = true
            restoreState = true

            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
        })
    }

    val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        when (destination.id) {
            R.id.historyFragment -> selectButton(btnFragmentHistory)
            R.id.graphsFragment -> selectButton(btnFragmentGraphs)
            R.id.reportFragment -> selectButton(btnFragmentReport)
            R.id.settingsFragment -> selectButton(btnFragmentSettings)
        }
    }

    navController.addOnDestinationChangedListener(listener)

    btnFragmentHistory.setOnClickListener {
        navigateSafe(R.id.historyFragment)
    }

    btnFragmentGraphs.setOnClickListener {
        selectButton(btnFragmentGraphs)
        navigateSafe(R.id.graphsFragment)
    }

    btnFragmentReport.setOnClickListener {
        selectButton(btnFragmentReport)
        navigateSafe(R.id.reportFragment)
    }

    btnFragmentSettings.setOnClickListener {
        selectButton(btnFragmentSettings)
        navigateSafe(R.id.settingsFragment)
    }

    btnAddNewOperation.setOnClickListener {
        val intent = Intent(requireContext(), AddNewOperationActivity::class.java)
        startActivity(intent)
    }

    viewLifecycleOwner.lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                navController.removeOnDestinationChangedListener(listener)
            }
        }
    )
}