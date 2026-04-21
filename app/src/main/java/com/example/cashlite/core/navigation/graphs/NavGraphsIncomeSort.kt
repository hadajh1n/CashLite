package com.example.cashlite.core.navigation.graphs

import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentGraphsIncomeBinding

fun Fragment.setupGraphsIncomeSortNav(
    binding: FragmentGraphsIncomeBinding
) = with(binding) {

    val navHostFragment = childFragmentManager
        .findFragmentById(R.id.nav_graphs_income_sort) as? NavHostFragment
        ?: return@with

    val navController = navHostFragment.navController

    fun selectButton(selected: View) {
        btnWeek.isSelected = false
        btnMonth.isSelected = false
        btnYear.isSelected = false
        selected.isSelected = true
    }

    fun navigateSafe(destinationId: Int) {
        if (navController.currentDestination?.id == destinationId) return

        navController.navigate(destinationId, null, navOptions {
            launchSingleTop = true
        })
    }

    val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        when (destination.id) {
            R.id.graphsIncomeWeekFragment -> selectButton(btnWeek)
            R.id.graphsIncomeMonthFragment -> selectButton(btnMonth)
            R.id.graphsIncomeYearFragment -> selectButton(btnYear)
        }
    }

    navController.addOnDestinationChangedListener(listener)

    btnWeek.setOnClickListener {
        navigateSafe(R.id.graphsIncomeWeekFragment)
    }

    btnMonth.setOnClickListener {
        navigateSafe(R.id.graphsIncomeMonthFragment)
    }

    btnYear.setOnClickListener {
        navigateSafe(R.id.graphsIncomeYearFragment)
    }

    viewLifecycleOwner.lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                navController.removeOnDestinationChangedListener(listener)
            }
        }
    )
}