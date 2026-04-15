package com.example.cashlite.data.mapper

import com.example.cashlite.data.dataclass.ParseBankTransaction
import com.example.cashlite.data.local.TransactionClassifier
import com.example.cashlite.data.repository.AppRepository
import com.example.cashlite.data.room.transaction.TransactionEntity
import kotlin.math.abs

class TransactionImportMapper {

    suspend fun mapToEntity(parsed: ParseBankTransaction): TransactionEntity? {

        val categoryKey = TransactionClassifier.classify(parsed)

        val category = AppRepository.getCategoryByName(categoryKey)
            ?: return null

        return TransactionEntity(
            idCategory = category.idCategory,
            amount = abs(parsed.amount),
            note = parsed.note,
            date = parsed.date,
            contact = null,
            isImport = true
        )
    }
}