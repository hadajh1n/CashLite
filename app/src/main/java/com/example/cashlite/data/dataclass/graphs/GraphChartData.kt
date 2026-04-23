package com.example.cashlite.data.dataclass.graphs

import com.github.mikephil.charting.data.PieEntry

data class GraphChartData(
    val pieEntries: List<PieEntry>,
    val categoryColors: List<Int>,
    val categoryDetails: List<CategoryChartItem>
)