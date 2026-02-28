package com.example.cashlite.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.IconExpenseCategory
import com.example.cashlite.data.repository.AppRepository

class AddExpenseViewModel : ViewModel() {

    private var _expenseCategories = MutableLiveData<List<IconExpenseCategory>>()
    val expenseCategories: LiveData<List<IconExpenseCategory>> get() = _expenseCategories

    private var isInitExpenseCategories = false

    fun initExpenseCategory() {
        if (isInitExpenseCategories) return

        _expenseCategories.value = AppRepository.getExpenseCategories()
        isInitExpenseCategories = true
    }

    fun addExpenseOperation(category: IconExpenseCategory, amount: Double, note: String) {
        AppRepository.addExpenseTransaction(category, amount, note)
    }

}