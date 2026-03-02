package com.example.cashlite.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.data.repository.AppRepository

class AddExpenseViewModel : ViewModel() {

    private var _expenseCategories = MutableLiveData<List<Transaction.Expense>>()
    val expenseCategories: LiveData<List<Transaction.Expense>> get() = _expenseCategories

    private var isInitExpenseCategories = false

    fun initExpenseCategory() {
        if (isInitExpenseCategories) return

        _expenseCategories.value = AppRepository.getExpenseCategories()
        isInitExpenseCategories = true
    }

    fun addExpenseOperation(category: Transaction.Expense, amount: Double, note: String) {
        AppRepository.addExpenseTransaction(category, amount, note)
    }

    fun totalExpense(amount: Double) {
        AppRepository.updateTotalExpense(amount)
        AppRepository.updateTotals()
    }
}