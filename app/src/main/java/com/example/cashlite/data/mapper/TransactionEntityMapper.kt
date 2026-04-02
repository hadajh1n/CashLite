package com.example.cashlite.data.mapper

import com.example.cashlite.data.room.category.CategoryEntity
import com.example.cashlite.data.room.transaction.TransactionEntity

class TransactionEntityMapper {

    fun transactionToEntity(
        category: CategoryEntity,
        defaultAmount: Double = 0.0
    ): TransactionEntity {

        return TransactionEntity(
            idCategory = category.idCategory,
            amount = defaultAmount,
            note = "",
        )
    }
}