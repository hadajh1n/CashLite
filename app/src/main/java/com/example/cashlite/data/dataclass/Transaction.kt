package com.example.cashlite.data.dataclass

sealed class Transaction {

    data class Expense(
        val imageId: Int,
        val categoryName: String,
        val amount: Double = 0.0,
        val note: String = ""
    ) : Transaction()

    data class Income(
        val imageId: Int,
        val categoryName: String,
        val amount: Double = 0.0,
        val note: String = ""
    ) : Transaction()
}