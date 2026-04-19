package com.example.cashlite.data.dataclass

import android.os.Parcelable
import com.example.cashlite.data.room.category.CategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionUI(
    val idTransaction: Int,
    val imageId: Int,
    val color: Int,
    val categoryName: String,
    val type: CategoryType,
    val amount: Double,
    val note: String? = null,
    val date: Long,
    val contact: String? = null,
) : Parcelable