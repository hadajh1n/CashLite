package com.example.cashlite.data.dataclass.graphs

import com.example.cashlite.data.room.category.CategoryType

data class CategoryChartItem(
    val categoryName: String,
    val imageId: Int,
    val color: Int,
    val type: CategoryType,
    val amount: Double,
    val percent: Double
)