package com.example.cashlite.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.data.repository.AppRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        val date = getCurrentDate()
        AppRepository.addExpenseTransaction(category, amount, note, date)
    }

    fun totalExpense(amount: Double) {
        AppRepository.updateTotalExpense(amount)
        AppRepository.updateTotals()
    }

    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        return LocalDate.now().format(formatter)
    }
}