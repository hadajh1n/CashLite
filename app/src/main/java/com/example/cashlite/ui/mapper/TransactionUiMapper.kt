package com.example.cashlite.ui.mapper

import com.example.cashlite.data.dataclass.TransactionUI
import com.example.cashlite.data.room.category.CategoryEntity
import com.example.cashlite.data.room.transaction.TransactionEntity

class TransactionUiMapper {

    fun fromEntityToUI(
        entityTrans: TransactionEntity,
        entityCategory: CategoryEntity
    ): TransactionUI {

        return TransactionUI(
            idTransaction = entityTrans.idTransaction,
            imageId = entityCategory.imageId,
            color = entityCategory.color,
            categoryName = entityCategory.categoryName,
            amount = entityTrans.amount,
            note = entityTrans.note,
            date = entityTrans.date,
            contact = entityTrans.contact,
        )
    }
}