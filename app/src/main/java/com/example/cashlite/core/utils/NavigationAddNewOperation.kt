package com.example.cashlite.core.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.cashlite.R
import com.example.cashlite.databinding.ActivityAddNewOperationBinding

fun AppCompatActivity.setupAddOperationNavigation(
    binding: ActivityAddNewOperationBinding
) = with(binding) {

    val navHostFragment = supportFragmentManager
        .findFragmentById(R.id.nav_host_add_new_operation_fragment) as NavHostFragment

    val navController = navHostFragment.navController

    fun selectButton(selected: View) = with(binding) {
        btnExpense.isSelected = false
        btnIncome.isSelected = false
        btnTransfer.isSelected = false

        selected.isSelected = true
    }

    btnExpense.setOnClickListener {
        selectButton(btnExpense)
        navController.navigate(R.id.addExpenceOperationFragment)
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