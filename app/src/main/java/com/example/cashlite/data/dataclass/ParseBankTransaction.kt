package com.example.cashlite.data.dataclass

data class ParseBankTransaction(
    val date: Long,
    val amount: Double,
    val note: String,
)