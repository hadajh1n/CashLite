package com.example.cashlite.ui.viewModel.graphs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.TransactionUI
import com.example.cashlite.data.repository.AppRepository
import com.github.mikephil.charting.data.PieEntry

class GraphsExpenseViewModel : ViewModel() {

//    private val transactions: LiveData<List<TransactionUI>> = AppRepository.transactions
//
//    val pieEntries: LiveData<List<PieEntry>> = MediatorLiveData<List<PieEntry>>().apply {
//        addSource(transactions) { list ->
//            value = buildExpensePieEntries(list)
//        }
//    }
//
//    private fun buildExpensePieEntries(transactions: List<TransactionUI>): List<PieEntry> {
//
////        Log.e("LogTest", "Построение графика расходов")
////        val expenseMap = transactions
////            .filterIsInstance<TransactionUI>()
////            .groupBy { it.categoryNameRes }
////            .mapValues { (_, list) -> list.sumOf { it.amount } }
////            .filterValues { it > 0.0 }
////
////        return expenseMap.map { (category, sum) ->
////            PieEntry(sum.toFloat(), category)
////        }
//    }
}