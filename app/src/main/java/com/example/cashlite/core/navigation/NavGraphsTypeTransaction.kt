package com.example.cashlite.core.navigation

import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentMainGraphsBinding

fun Fragment.setupGraphsTypeTransactionNav(
    binding: FragmentMainGraphsBinding
) = with(binding) {

    val navHostFragment = childFragmentManager
        .findFragmentById(R.id.nav_main_graphs_type) as NavHostFragment
    val navController = navHostFragment.navController

    fun selectButton(selected: View) {
        btnExpense.isSelected = false
        btnIncome.isSelected = false

        selected.isSelected = true
    }

    btnExpense.setOnClickListener {
        selectButton(btnExpense)
        navController.navigate(R.id.graphsExpenseFragment)
    }

    btnIncome.setOnClickListener {
        selectButton(btnIncome)
        navController.navigate(R.id.graphsIncomeFragment)
    }

    btnExpense.isSelected = true
}