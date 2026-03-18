package com.example.cashlite.core.navigation

import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.example.cashlite.R
import com.example.cashlite.databinding.FragmentAddOperationBinding

fun Fragment.setupAddOperationNav(
    binding: FragmentAddOperationBinding
) = with(binding) {

    val navHostFragment = childFragmentManager
        .findFragmentById(R.id.nav_host_category_operation_fragment) as NavHostFragment
    val navController = navHostFragment.navController

    fun selectButton(selected: View) {
        btnExpense.isSelected = false
        btnIncome.isSelected = false
        btnTransfer.isSelected = false

        selected.isSelected = true
    }

    btnExpense.setOnClickListener {
        selectButton(btnExpense)
        navController.navigate(R.id.addExpenseOperationFragment)
    }

    btnIncome.setOnClickListener {
        selectButton(btnIncome)
        navController.navigate(R.id.addIncomeOperationFragment)
    }

    btnTransfer.setOnClickListener {
        selectButton(btnTransfer)
        navController.navigate(R.id.addTransferOperationFragment)
    }

    btnExpense.isSelected = true
}