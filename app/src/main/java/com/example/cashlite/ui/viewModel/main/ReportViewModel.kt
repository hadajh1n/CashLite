package com.example.cashlite.ui.viewModel.main

import androidx.lifecycle.*
import com.example.cashlite.data.dataclass.history.TransactionUI
import com.example.cashlite.data.dataclass.report.ReportData
import com.example.cashlite.data.repository.AppRepository
import com.example.cashlite.data.room.category.CategoryType
import java.util.Calendar

class ReportViewModel : ViewModel() {

    companion object {
        private const val INTEREST_TRANSFER = 100
    }

    private val transactions: LiveData<List<TransactionUI>> = AppRepository.transactions

    val reportData: LiveData<ReportData> = transactions.map { list ->
        calculateReport(list)
    }

    private fun calculateReport(allTransactions: List<TransactionUI>): ReportData {
        val cal = Calendar.getInstance()
        val currentMonth = cal.get(Calendar.MONTH)
        val currentYear = cal.get(Calendar.YEAR)

        val monthList = allTransactions.filter {
            val tCal = Calendar.getInstance().apply { timeInMillis = it.date }
            tCal.get(Calendar.MONTH) == currentMonth && tCal.get(Calendar.YEAR) == currentYear
        }

        if (monthList.isEmpty()) return ReportData()

        val income = monthList.filter { it.type == CategoryType.INCOME }.sumOf { it.amount }
        val expense = monthList.filter { it.type == CategoryType.EXPENSE }.sumOf { it.amount }

        val savingsRate = if (income > 0) ((income - expense) / income * INTEREST_TRANSFER)
            .coerceAtLeast(0.0) else 0.0

        val topCategories = monthList.filter { it.type == CategoryType.EXPENSE }
            .groupBy { it.categoryName }
            .mapValues { it.value.sumOf { t -> t.amount } }
            .toList()
            .sortedByDescending { it.second }
            .take(3)

        val maxExpense = monthList.filter { it.type == CategoryType.EXPENSE }
            .maxByOrNull { it.amount }
            ?.let { it.categoryName to it.amount }

        return ReportData(
            totalIncome = income,
            totalExpense = expense,
            savingsRate = savingsRate,
            averageTransaction = if (monthList.isNotEmpty()) monthList.sumOf { it.amount } /
                    monthList.size else 0.0,
            transactionCount = monthList.size,
            maxExpense = maxExpense,
            topCategories = topCategories
        )
    }
}