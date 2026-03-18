package com.example.cashlite.ui.viewModel.newOperation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.data.repository.AppRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        val date = getCurrentDate()
        AppRepository.addIncomeTransaction(category, amount, note, date)
    }

    fun totalIncome(amount: Double) {
        AppRepository.updateTotalIncome(amount)
        AppRepository.updateTotals()
    }

    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        return LocalDate.now().format(formatter)
    }
}