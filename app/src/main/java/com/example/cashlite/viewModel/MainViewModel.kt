package com.example.cashlite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.Tab

class MainViewModel : ViewModel() {

    private val _selectedTab = MutableLiveData<Tab>(Tab.HISTORY)

    val selectedTab: LiveData<Tab> get() = _selectedTab

    fun selectTab(tab: Tab) {
        _selectedTab.value = tab
    }
}