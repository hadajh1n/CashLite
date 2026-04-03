package com.example.cashlite.ui.viewModel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.data.repository.AppRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var isInitSystemCategories = false

    fun initSystemCategories() {
        if (isInitSystemCategories) {
            return
        }
        isInitSystemCategories = true

        viewModelScope.launch {
            AppRepository.initCategories()
        }
    }
}