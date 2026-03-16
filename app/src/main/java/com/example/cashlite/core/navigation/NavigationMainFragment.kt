package com.example.cashlite.core.navigation

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentMainBinding

fun Fragment.setupBottomNavigation(
    binding: FragmentMainBinding
) = with(binding) {

    val navHostFragment = childFragmentManager
        .findFragmentById(R.id.nav_host_main_set_fragment) as NavHostFragment

    val navController = navHostFragment.navController

    btnFragmentHistory.isSelected = true

    fun selectButton(selected: View) {
        btnFragmentHistory.isSelected = false
        btnFragmentGraphs.isSelected = false
        btnFragmentReport.isSelected = false
        btnFragmentSettings.isSelected = false

        selected.isSelected = true
    }

    btnFragmentHistory.setOnClickListener {
        selectButton(btnFragmentHistory)
        navController.navigate(R.id.historyFragment)
    }

    btnFragmentGraphs.setOnClickListener {
        selectButton(btnFragmentGraphs)
        navController.navigate(R.id.graphsFragment)
    }

    btnAddNewOperation.setOnClickListener {
        navController.navigate(R.id.addNewOperationActivity)
    }

    btnFragmentReport.setOnClickListener {
        selectButton(btnFragmentReport)
        navController.navigate(R.id.reportFragment)
    }

    btnFragmentSettings.setOnClickListener {
        selectButton(btnFragmentSettings)
        navController.navigate(R.id.settingsFragment)
    }
}