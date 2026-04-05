package com.example.cashlite.data.mapper

import com.example.cashlite.data.dataclass.CategoryUI
import com.example.cashlite.data.room.transaction.TransactionEntity

class TransactionEntityMapper {

    fun transactionToEntity(
        category: CategoryUI,
        amount: Double,
        note: String,
        date: Long,
    ): TransactionEntity {

        return TransactionEntity(
            idCategory = category.idCategory,
            amount = amount,
            note = note,
            date = date,
            isImport = false,
        )
    }
}