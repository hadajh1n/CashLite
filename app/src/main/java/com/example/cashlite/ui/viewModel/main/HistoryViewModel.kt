package com.example.cashlite.ui.viewModel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.core.utils.format.formatDate
import com.example.cashlite.data.dataclass.HistoryItem
import com.example.cashlite.data.dataclass.TotalsStateUI
import com.example.cashlite.data.dataclass.TransactionUI
import com.example.cashlite.data.repository.AppRepository
import kotlinx.coroutines.launch
import kotlin.collections.iterator

class HistoryViewModel : ViewModel() {

    private val transactions: LiveData<List<TransactionUI>> = AppRepository.transactions
    val totalTransaction: LiveData<TotalsStateUI> = AppRepository.totalTransaction

    val historyItems: LiveData<List<HistoryItem>> = MediatorLiveData<List<HistoryItem>>().apply {
        addSource(transactions) { input ->
            value = buildHistoryItems(input)
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
}