package com.example.cashlite.data.room.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val idCategory: Int = 0,
    val type: CategoryType,
    val categoryName: String,
    val imageId: Int,
    val color: Int,
    val isSystem: Boolean = false,
)