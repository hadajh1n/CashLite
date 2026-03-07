package com.example.cashlite.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.HistoryItem
import com.example.cashlite.data.dataclass.TotalsState
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.data.repository.AppRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")

        val sortedTransactions =
            transactions.sortedWith(compareByDescending { LocalDate.parse(it.date, formatter) })

        val historyItems = mutableListOf<HistoryItem>()

        var currentDate = sortedTransactions.first().date
        historyItems.add(HistoryItem.DateHeader(currentDate))

        for (transaction in sortedTransactions) {
            if (transaction.date != currentDate) {
                currentDate = transaction.date
                historyItems.add(HistoryItem.DateHeader(currentDate))
            }
            historyItems.add(HistoryItem.TransactionItem(transaction))
        }

        return historyItems
    }
}