package com.example.cashlite.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.TotalsState
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.data.repository.AppRepository

class HistoryViewModel : ViewModel() {
    val transactions: LiveData<List<Transaction>> = AppRepository.transactions
    val totalTransaction: LiveData<TotalsState> = AppRepository.updateTotalTransactions
}