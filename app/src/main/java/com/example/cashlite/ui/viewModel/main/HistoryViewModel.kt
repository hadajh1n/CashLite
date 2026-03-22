package com.example.cashlite.ui.viewModel.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.HistoryItem
import com.example.cashlite.data.dataclass.TotalsState
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.data.repository.AppRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.iterator

class HistoryViewModel : ViewModel() {

    private val transactions: LiveData<List<Transaction>> = AppRepository.transactions
    val totalTransaction: LiveData<TotalsState> = AppRepository.updateTotalTransactions

    val historyItems: LiveData<List<HistoryItem>> = MediatorLiveData<List<HistoryItem>>().apply {
        addSource(transactions) { input ->
            value = buildHistoryItems(input)
        }
    }

    private fun buildHistoryItems(transactions: List<Transaction>): List<HistoryItem> {
        if (transactions.isEmpty()) return emptyList()

        Log.e("LogTest", "Построение списка истории")

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")

        val sorted = transactions.sortedByDescending {
            LocalDate.parse(it.date, formatter)
        }

        val grouped = sorted.groupBy { it.date }
        val result = mutableListOf<HistoryItem>()

        for ((date, list) in grouped) {
            result.add(HistoryItem.DateHeader(date))

            list.forEach { transaction ->
                result.add(HistoryItem.TransactionItem(transaction))
            }
        }

        return result
    }

    fun onSwipeRemoveTransaction(transaction: Transaction) {
        Log.e("LogTest", "Удаление транзакции")
        AppRepository.removeTransaction(transaction)
    }
}