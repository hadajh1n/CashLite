package com.example.cashlite.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.IconIncomeCategory
import com.example.cashlite.data.repository.AppRepository

class AddIncomeViewModel : ViewModel() {

    private var _incomeCategories = MutableLiveData<List<IconIncomeCategory>>()
    val incomeCategories: LiveData<List<IconIncomeCategory>> get() = _incomeCategories

    private var isInitIncomeCategories = false

    fun initIncomeCategory() {
        if (isInitIncomeCategories) return

        _incomeCategories.value = AppRepository.getIncomeCategories()
        isInitIncomeCategories = true
    }

    fun addIncomeOperation(category: IconIncomeCategory, amount: Double, note: String) {
        AppRepository.addIncomeTransaction(category, amount, note)
    }

}