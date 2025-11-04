package com.example.cashlite.core.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cashlite.R
import com.example.cashlite.databinding.ActivityMainBinding

fun AppCompatActivity.setupBottomNavigation(binding: ActivityMainBinding) {
    val navHostFragment = supportFragmentManager
        .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment ?: return
    val navController = navHostFragment.navController
    binding.bottomNavigation.setupWithNavController(navController)
}