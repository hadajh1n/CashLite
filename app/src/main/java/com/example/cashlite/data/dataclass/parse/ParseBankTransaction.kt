package com.example.cashlite.data.dataclass.parse

data class ParseBankTransaction(
    val date: Long,
    val amount: Double,
    val rawNote: String,
    val displayNote: String,
)