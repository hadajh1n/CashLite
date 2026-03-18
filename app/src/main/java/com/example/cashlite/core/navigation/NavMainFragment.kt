package com.example.cashlite.core.navigation

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentMainBinding
import com.example.cashlite.ui.activity.AddNewOperationActivity

fun Fragment.setupBottomNav(binding: FragmentMainBinding) = with(binding) {

    val navHostFragment = childFragmentManager
        .findFragmentById(R.id.nav_host_main_set_fragment) as? NavHostFragment
        ?: return@with

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

    btnFragmentReport.setOnClickListener {
        selectButton(btnFragmentReport)
        navController.navigate(R.id.reportFragment)
    }

    btnFragmentSettings.setOnClickListener {
        selectButton(btnFragmentSettings)
        navController.navigate(R.id.settingsFragment)
    }

    btnAddNewOperation.setOnClickListener {
        val intent = Intent(requireContext(), AddNewOperationActivity::class.java)
        startActivity(intent)
    }
}