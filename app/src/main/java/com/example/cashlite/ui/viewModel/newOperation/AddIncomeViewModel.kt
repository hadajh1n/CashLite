package com.example.cashlite.ui.viewModel.newOperation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.data.dataclass.history.CategoryUI
import com.example.cashlite.data.repository.AppRepository
import kotlinx.coroutines.launch

class AddIncomeViewModel : ViewModel() {

    val incomeCategories: LiveData<List<CategoryUI>> = AppRepository.incomeCategories

    fun addIncomeOperation(category: CategoryUI, amount: Double, note: String, date: Long) {
        viewModelScope.launch {
            AppRepository.addTransaction(category, amount, note, date)
        }
    }
}