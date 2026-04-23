package com.example.cashlite.data.dataclass.graphs

import com.example.cashlite.data.room.category.CategoryType

enum class Period {
    WEEK, MONTH, YEAR
}

data class GraphState(
    val type: CategoryType = CategoryType.EXPENSE,
    val period: Period = Period.YEAR,
    val range: String? = null
)