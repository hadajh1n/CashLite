package com.example.cashlite.ui.viewModel.newOperation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.data.dataclass.CategoryUI
import com.example.cashlite.data.repository.AppRepository
import kotlinx.coroutines.launch

class AddExpenseViewModel : ViewModel() {

    val expenseCategories: LiveData<List<CategoryUI>> = AppRepository.expenseCategories

    fun addExpenseOperation(category: CategoryUI, amount: Double, note: String, date: Long) {
        viewModelScope.launch {
            AppRepository.addTransaction(category, amount, note, date)
        }
    }
}