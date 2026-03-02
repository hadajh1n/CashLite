package com.example.cashlite.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.data.repository.AppRepository

class AddIncomeViewModel : ViewModel() {

    private var _incomeCategories = MutableLiveData<List<Transaction.Income>>()
    val incomeCategories: LiveData<List<Transaction.Income>> get() = _incomeCategories

    private var isInitIncomeCategories = false

    fun initIncomeCategory() {
        if (isInitIncomeCategories) return

        _incomeCategories.value = AppRepository.getIncomeCategories()
        isInitIncomeCategories = true
    }

    fun addIncomeOperation(category: Transaction.Income, amount: Double, note: String) {
        AppRepository.addIncomeTransaction(category, amount, note)
    }

    fun totalIncome(amount: Double) {
        AppRepository.updateTotalIncome(amount)
        AppRepository.updateTotals()
    }

}