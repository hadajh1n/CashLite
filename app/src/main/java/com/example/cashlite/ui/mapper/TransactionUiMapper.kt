package com.example.cashlite.ui.mapper

import com.example.cashlite.data.dataclass.TransactionUI
import com.example.cashlite.data.room.category.CategoryEntity
import com.example.cashlite.data.room.transaction.TransactionEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TransactionUiMapper {

    fun fromEntityToUI(
        entityTrans: TransactionEntity,
        entityCategory: CategoryEntity
    ): TransactionUI {
        val formattedDate = formatDate(entityTrans.date)

        return TransactionUI(
            idTransaction = entityTrans.idTransaction,
            imageId = entityCategory.imageId,
            color = entityCategory.color,
            categoryName = entityCategory.categoryName,
            amount = entityTrans.amount,
            note = entityTrans.note,
            date = formattedDate,
        )
    }

    private fun formatDate(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
    }
}