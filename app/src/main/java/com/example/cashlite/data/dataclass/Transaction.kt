package com.example.cashlite.data.dataclass

sealed class Transaction {
    abstract val date: String

    data class Expense(
        val imageId: Int,
        val color: Int,
        val categoryName: String,
        val amount: Double = 0.0,
        val note: String = "",
        override val date: String = "",
    ) : Transaction()

    data class Income(
        val imageId: Int,
        val color: Int,
        val categoryName: String,
        val amount: Double = 0.0,
        val note: String = "",
        override val date: String = "",
    ) : Transaction()
}