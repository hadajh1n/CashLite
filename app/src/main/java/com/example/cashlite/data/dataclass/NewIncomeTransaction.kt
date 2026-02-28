package com.example.cashlite.data.dataclass

data class NewIncomeTransaction(
    val category: IconIncomeCategory,
    val amount: Double,
    val note: String,
)