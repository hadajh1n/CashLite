package com.example.cashlite.ui.viewModel.newOperation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.data.repository.AppRepository
import kotlinx.coroutines.launch

class AddTransferViewModel : ViewModel() {

    fun addOperationTransfer(
        categoryKey: String,
        amount: Double,
        note: String,
        date: Long,
        contact: String,
    ) {
        viewModelScope.launch {
            val category = AppRepository.getCategoryByName(categoryKey)
                ?: return@launch

            AppRepository.addTransaction(category, amount, note, date, contact)
        }
    }
}