package com.example.cashlite.ui.viewModel.graphs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.data.repository.AppRepository
import com.github.mikephil.charting.data.PieEntry

class GraphsIncomeViewModel : ViewModel() {

    private val transactions: LiveData<List<Transaction>> = AppRepository.transactions

    val pieEntries: LiveData<List<PieEntry>> = MediatorLiveData<List<PieEntry>>().apply {
        addSource(transactions) { list ->
            value = buildIncomePieEntries(list)
        }
    }

    private fun buildIncomePieEntries(transactions: List<Transaction>): List<PieEntry> {
        val incomeMap = transactions
            .filterIsInstance<Transaction.Income>()
            .groupBy { it.categoryName }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
            .filterValues { it > 0.0 }

        return incomeMap.map { (category, sum) ->
            PieEntry(sum.toFloat(), category)
        }
    }
}