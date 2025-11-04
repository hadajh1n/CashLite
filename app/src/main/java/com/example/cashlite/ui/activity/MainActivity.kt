package com.example.cashlite.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cashlite.core.utils.setupBottomNavigation
import com.example.cashlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation(binding)
    }
}