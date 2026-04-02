package com.example.cashlite.ui.viewModel.graphs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.cashlite.data.dataclass.TransactionUI
import com.example.cashlite.data.repository.AppRepository
import com.github.mikephil.charting.data.PieEntry

class GraphsIncomeViewModel : ViewModel() {

//    private val transactions: LiveData<List<TransactionUI>> = AppRepository.transactions
//
//    val pieEntries: LiveData<List<PieEntry>> = MediatorLiveData<List<PieEntry>>().apply {
//        addSource(transactions) { list ->
//            value = buildIncomePieEntries(list)
//        }
//    }
//
//    private fun buildIncomePieEntries(transactions: List<TransactionUI>): List<PieEntry> {
//
////        Log.e("LogTest", "Построение графика доходов")
////        val incomeMap = transactions
////            .filterIsInstance<TransactionUI.Income>()
////            .groupBy { it.categoryNameRes }
////            .mapValues { (_, list) -> list.sumOf { it.amount } }
////            .filterValues { it > 0.0 }
////
////        return incomeMap.map { (category, sum) ->
////            PieEntry(sum.toFloat(), category)
////        }
//    }
}