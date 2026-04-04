package com.example.cashlite.core.navigation

import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentAddOperationBinding

fun Fragment.setupAddOperationNav(
    binding: FragmentAddOperationBinding
) = with(binding) {

    val navHostFragment = childFragmentManager
        .findFragmentById(R.id.nav_host_category_operation_fragment) as? NavHostFragment
        ?: return@with

    val navController = navHostFragment.navController

    fun selectButton(selected: View) {
        btnExpense.isSelected = false
        btnIncome.isSelected = false
        btnTransfer.isSelected = false
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
            R.id.addExpenseOperationFragment -> selectButton(btnExpense)
            R.id.addIncomeOperationFragment -> selectButton(btnIncome)
            R.id.addTransferOperationFragment -> selectButton(btnTransfer)
        }
    }

    navController.addOnDestinationChangedListener(listener)

    btnExpense.setOnClickListener {
        navigateSafe(R.id.addExpenseOperationFragment)
    }

    btnIncome.setOnClickListener {
        navigateSafe(R.id.addIncomeOperationFragment)
    }

    btnTransfer.setOnClickListener {
        navigateSafe(R.id.addTransferOperationFragment)
    }

    viewLifecycleOwner.lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                navController.removeOnDestinationChangedListener(listener)
            }
        }
    )
}