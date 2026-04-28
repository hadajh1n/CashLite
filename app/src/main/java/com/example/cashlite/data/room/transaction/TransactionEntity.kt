package com.example.cashlite.data.room.transaction

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.cashlite.data.room.category.CategoryEntity
import com.example.cashlite.data.room.importing.ImportedStatementEntity

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["idCategory"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = ImportedStatementEntity::class,
            parentColumns = ["signature"],
            childColumns = ["signature"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val idTransaction: Int = 0,
    val idCategory: Int,
    val signature: String? = null,
    val amount: Double,
    val note: String? = null,
    val date: Long = System.currentTimeMillis(),
    val contact: String? = null,
    val isImport: Boolean = false,
)