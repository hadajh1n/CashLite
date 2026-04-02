package com.example.cashlite.data.dataclass

data class TransactionUI(
    val idTransaction: Int,
    val imageId: Int,
    val color: Int,
    val categoryName: String,
    val amount: Double,
    val note: String? = null,
    val date: String,
)