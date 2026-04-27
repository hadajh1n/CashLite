package com.example.cashlite.data.dataclass.report

data class ReportData(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val savingsRate: Double = 0.0,
    val averageTransaction: Double = 0.0,
    val transactionCount: Int = 0,
    val maxExpense: Pair<String, Double>? = null,
    val topCategories: List<Pair<String, Double>> = emptyList()
)