package com.example.cashlite.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cashlite.core.utils.setupAddOperationNavigation
import com.example.cashlite.databinding.ActivityAddNewOperationBinding

class AddNewOperationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddNewOperationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewOperationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAddOperationNavigation(binding)
    }
}