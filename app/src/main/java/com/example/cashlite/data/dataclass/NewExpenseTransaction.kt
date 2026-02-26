package com.example.cashlite.data.dataclass

data class NewExpenseTransaction(
    val category: IconExpenseCategory,
    val amount: Double,
    val note: String,
)