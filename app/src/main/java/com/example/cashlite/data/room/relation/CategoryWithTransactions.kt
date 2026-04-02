package com.example.cashlite.data.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.cashlite.data.room.category.CategoryEntity
import com.example.cashlite.data.room.transaction.TransactionEntity

data class CategoryWithTransactions(
    @Embedded
    val category: CategoryEntity,

    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    val transactions: List<TransactionEntity>
)