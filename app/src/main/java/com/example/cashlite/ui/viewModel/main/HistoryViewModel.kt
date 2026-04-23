package com.example.cashlite.ui.viewModel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.core.utils.format.formatDate
import com.example.cashlite.data.dataclass.history.HistoryItem
import com.example.cashlite.data.dataclass.history.TotalsStateUI
import com.example.cashlite.data.dataclass.history.TransactionUI
import com.example.cashlite.data.repository.AppRepository
import kotlinx.coroutines.launch
import kotlin.collections.iterator

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    object Empty : HistoryUiState()
    data class Content(val items: List<HistoryItem>) : HistoryUiState()
}

class HistoryViewModel : ViewModel() {

    private val transactions: LiveData<List<TransactionUI>> = AppRepository.transactions
    val totalTransaction: LiveData<TotalsStateUI> = AppRepository.totalTransaction

    val uiHistoryState: LiveData<HistoryUiState> = MediatorLiveData<HistoryUiState>().apply {

        value = HistoryUiState.Loading

        addSource(transactions) { list ->
            value = when {
                list.isEmpty() -> HistoryUiState.Empty
                else -> HistoryUiState.Content(buildHistoryItems(list))
            }
        }
    }

    private fun buildHistoryItems(transactions: List<TransactionUI>): List<HistoryItem> {
        if (transactions.isEmpty()) return emptyList()

        val sorted = transactions.sortedByDescending { it.date }
        val grouped = sorted.groupBy { formatDate(it.date) }
        val result = mutableListOf<HistoryItem>()

        for ((date, list) in grouped) {
            result.add(HistoryItem.DateHeader(date))

            list.forEach { transaction ->
                result.add(HistoryItem.TransactionItem(transaction))
            }
        }
        return result
    }

    fun onSwipeRemoveTransaction(transaction: TransactionUI) {
        viewModelScope.launch {
            AppRepository.removeTransaction(transaction.idTransaction)
        }
    }

    fun onDeleteAllTransactions() {
        viewModelScope.launch {
            AppRepository.deleteAllTransactions()
        }
    }

    fun onDeleteImportedTransactions() {
        viewModelScope.launch {
            AppRepository.deleteImportedTransactions()
        }
    }
}