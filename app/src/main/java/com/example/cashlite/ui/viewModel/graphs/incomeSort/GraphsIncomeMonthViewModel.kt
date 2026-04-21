package com.example.cashlite.ui.viewModel.graphs.incomeSort

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.core.app.CashLiteApp
import com.example.cashlite.data.dataclass.TransactionUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.repository.AppRepository
import com.example.cashlite.data.room.category.CategoryType
import com.github.mikephil.charting.data.PieEntry

class GraphsIncomeMonthViewModel : ViewModel() {

    private val transactions: LiveData<List<TransactionUI>> = AppRepository.transactions

    val pieEntries: LiveData<List<PieEntry>> = MediatorLiveData<List<PieEntry>>().apply {
        addSource(transactions) { list ->
            value = buildIncomePieEntries(list)
        }
    }

    private fun buildIncomePieEntries(transactions: List<TransactionUI>): List<PieEntry> {

        Log.e("LogTest", "Построение графика расходов")
        val expenseMap = transactions
            .filter { it.type == CategoryType.INCOME }
            .groupBy { it.categoryName }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
            .filterValues { it > 0.0 }

        return expenseMap.map { (categoryKey, sum) ->
            val displayName = CashLiteApp.instance.getString(
                CategoryKeys.getCategoryNameRes(categoryKey)
            )
            PieEntry(sum.toFloat(), displayName)
        }
    }
}