package com.example.cashlite.viewModel

import androidx.lifecycle.ViewModel
import com.example.cashlite.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _selectedTab = MutableStateFlow(Tab.HISTORY)
    val selectedTab: StateFlow<Tab> get() = _selectedTab

    fun selectTab(tab: Tab) {
        _selectedTab.value = tab
    }
}