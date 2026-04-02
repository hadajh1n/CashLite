package com.example.cashlite.data.dataclass

sealed class HistoryItem {

    data class DateHeader(
        val date: String,
    ) : HistoryItem()

    data class TransactionItem(
        val transaction: TransactionUI,
    ) : HistoryItem()
}