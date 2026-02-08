package com.example.cashlite.core.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cashlite.R
import com.example.cashlite.databinding.ActivityAddNewOperationBinding

fun AppCompatActivity.setupAddOperationNavigation(binding: ActivityAddNewOperationBinding) {
    val navHostFragment = supportFragmentManager
        .findFragmentById(R.id.nav_host_add_new_operation_fragment) as NavHostFragment

    binding.topNavigationOperation
        .setupWithNavController(navHostFragment.navController)
}
