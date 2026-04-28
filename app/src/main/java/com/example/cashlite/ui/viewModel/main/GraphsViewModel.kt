package com.example.cashlite.ui.viewModel.main

import android.graphics.Color
import androidx.lifecycle.*
import com.example.cashlite.core.app.CashLiteApp
import com.example.cashlite.core.utils.graphSort.GraphUtils
import com.example.cashlite.data.dataclass.graphs.*
import com.example.cashlite.data.dataclass.history.TransactionUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.repository.AppRepository
import com.example.cashlite.data.room.category.CategoryType
import com.github.mikephil.charting.data.PieEntry
import kotlin.math.abs

class GraphsViewModel : ViewModel() {

    private val transactions = AppRepository.transactions

    private val state = MutableLiveData(
        GraphState(
            type = CategoryType.EXPENSE,
            period = Period.MONTH,
            range = GraphUtils.getCurrentPeriodKey(Period.MONTH)
        )
    )

    val graphState: LiveData<GraphState> = state

    val periods = MediatorLiveData<List<GraphPeriodUI>>().apply {
        fun update() {
            val list = transactions.value ?: emptyList()
            val s = state.value ?: return
            val earliest = list.minByOrNull { it.date }?.date ?: System.currentTimeMillis()
            value = GraphUtils.generateContinuousPeriods(earliest, s.period)
        }
        addSource(transactions) { update() }
        addSource(state) { update() }
    }

    val graphData = MediatorLiveData<GraphChartData>().apply {
        fun update() {
            val list = transactions.value ?: emptyList()
            val s = state.value ?: return
            value = buildGraphData(list, s)
        }
        addSource(transactions) { update() }
        addSource(state) { update() }
    }

    fun setType(type: CategoryType) {
        val current = state.value ?: return

        if (current.type == type) return

        state.value = current.copy(type = type)
    }

    fun setPeriod(period: Period) {
        val current = state.value ?: return

        if (current.period == period) return

        state.value = current.copy(
            period = period,
            range = GraphUtils.getCurrentPeriodKey(period)
        )
    }

    fun setRange(range: String?) {
        val current = state.value ?: return

        if (current.range == range) return

        state.value = current.copy(range = range)
    }

    private fun buildGraphData(list: List<TransactionUI>, state: GraphState): GraphChartData {
        val selectedRange = state.range ?: GraphUtils.getCurrentPeriodKey(state.period)

        val timeFiltered = list.filter {
            it.type == state.type && GraphUtils.getPeriodKey(
                it.date, state.period
            ) == selectedRange
        }

        if (timeFiltered.isEmpty()) return emptyData()

        val grouped = timeFiltered.groupBy { it.categoryName }
        val rawDetails = grouped.mapNotNull { (name, items) ->
            val sum = abs(items.sumOf { it.amount })
            if (sum <= 0.0) return@mapNotNull null
            val sample = items.first()
            CategoryChartItem(
                categoryName = name,
                imageId = sample.imageId,
                color = if (sample.color == 0) Color.GRAY else sample.color,
                type = sample.type,
                amount = sum,
                percent = 0.0
            )
        }.sortedByDescending { it.amount }

        val total = rawDetails.sumOf { it.amount }
        val finalDetails = rawDetails
            .map { it.copy(percent = (it.amount / total) * 100.0) }
            .sortedByDescending { it.amount }

        return GraphChartData(
            pieEntries = emptyList(),
            categoryColors = finalDetails.map { it.color },
            categoryDetails = finalDetails
        )
    }

    private fun emptyData() = GraphChartData(emptyList(), emptyList(), emptyList())

    fun refresh() {
        state.value = state.value
    }
}