package com.example.cashlite.data.room.transaction

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.cashlite.data.room.category.CategoryEntity

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["idCategory"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val idTransaction: Int = 0,
    val idCategory: Int,
    val amount: Double,
    val note: String? = null,
    val date: Long = System.currentTimeMillis(),
    val contact: String? = null,
    val isImport: Boolean = false,
)