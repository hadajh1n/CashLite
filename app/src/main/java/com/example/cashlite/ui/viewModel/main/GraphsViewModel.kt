package com.example.cashlite.ui.viewModel.main

import androidx.lifecycle.*
import com.example.cashlite.core.app.CashLiteApp
import com.example.cashlite.data.dataclass.*
import com.example.cashlite.data.dataclass.graphs.CategoryChartItem
import com.example.cashlite.data.dataclass.graphs.GraphChartData
import com.example.cashlite.data.dataclass.graphs.GraphPeriodUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.repository.AppRepository
import com.example.cashlite.data.room.category.CategoryType
import com.github.mikephil.charting.data.PieEntry
import java.util.Calendar
import kotlin.math.abs

class GraphsViewModel : ViewModel() {

    private val transactions = AppRepository.transactions

    private val state = MutableLiveData(
        GraphState(
            type = CategoryType.EXPENSE,
            period = Period.MONTH,
            range = getCurrentPeriodKey(Period.MONTH)
        )
    )

    val graphState: LiveData<GraphState> = state

    val periods = MediatorLiveData<List<GraphPeriodUI>>().apply {
        fun update() {
            val list = transactions.value ?: emptyList()
            val s = state.value ?: return
            value = buildContinuousPeriods(list, s)
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
        state.value = current.copy(type = type)
    }

    fun setPeriod(period: Period) {
        val current = state.value ?: return
        state.value = current.copy(period = period, range = getCurrentPeriodKey(period))
    }

    fun setRange(range: String?) {
        state.value = state.value?.copy(range = range)
    }

    private fun getCurrentPeriodKey(period: Period): String = periodKey(System.currentTimeMillis(), period)

    private fun periodKey(date: Long, period: Period): String {
        val cal = Calendar.getInstance().apply { timeInMillis = date }
        return when (period) {
            Period.YEAR -> cal.get(Calendar.YEAR).toString()
            Period.MONTH -> {
                val y = cal.get(Calendar.YEAR)
                val m = (cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
                "$y-$m"
            }
            Period.WEEK -> {
                val y = cal.get(Calendar.YEAR)
                val w = cal.get(Calendar.WEEK_OF_YEAR).toString().padStart(2, '0')
                "$y-$w"
            }
        }
    }

    private fun buildContinuousPeriods(list: List<TransactionUI>, state: GraphState): List<GraphPeriodUI> {
        val earliestDate = list.minByOrNull { it.date }?.date ?: System.currentTimeMillis()
        val currentCal = Calendar.getInstance()
        val earliestCal = Calendar.getInstance().apply { timeInMillis = earliestDate }

        currentCal.set(Calendar.HOUR_OF_DAY, 0)
        earliestCal.set(Calendar.HOUR_OF_DAY, 0)

        val resultKeys = mutableListOf<String>()
        val tempCal = Calendar.getInstance().apply { timeInMillis = currentCal.timeInMillis }

        while (tempCal.after(earliestCal) || isSamePeriod(tempCal, earliestCal, state.period)) {
            val key = periodKey(tempCal.timeInMillis, state.period)
            if (!resultKeys.contains(key)) {
                resultKeys.add(key)
            }
            when (state.period) {
                Period.WEEK -> tempCal.add(Calendar.WEEK_OF_YEAR, -1)
                Period.MONTH -> tempCal.add(Calendar.MONTH, -1)
                Period.YEAR -> tempCal.add(Calendar.YEAR, -1)
            }
            if (resultKeys.size > 100) break
        }

        // ПЕРЕВОРАЧИВАЕМ СПИСОК: теперь Январь будет слева, а Апрель справа
        return resultKeys.reversed().map {
            GraphPeriodUI(key = it, label = formatLabel(it, state.period))
        }
    }

    private fun isSamePeriod(c1: Calendar, c2: Calendar, period: Period): Boolean {
        return when (period) {
            Period.YEAR -> c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            Period.MONTH -> c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
            Period.WEEK -> c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR)
        }
    }

    private fun buildGraphData(list: List<TransactionUI>, state: GraphState): GraphChartData {
        val selectedRange = state.range ?: getCurrentPeriodKey(state.period)
        val timeFiltered = list.filter {
            it.type == state.type && periodKey(it.date, state.period) == selectedRange
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
                color = if (sample.color == 0) android.graphics.Color.GRAY else sample.color,
                type = sample.type,
                amount = sum,
                percent = 0.0
            )
        }.sortedByDescending { it.amount }

        val total = rawDetails.sumOf { it.amount }
        val finalDetails = rawDetails.map { it.copy(percent = (it.amount / total) * 100.0) }

        return GraphChartData(
            pieEntries = finalDetails.map { item ->
                val title = try {
                    CashLiteApp.instance.getString(CategoryKeys.getCategoryNameRes(item.categoryName))
                } catch (e: Exception) { item.categoryName }
                PieEntry(item.amount.toFloat(), title)
            },
            categoryColors = finalDetails.map { it.color },
            categoryDetails = finalDetails
        )
    }

    private fun emptyData() = GraphChartData(emptyList(), emptyList(), emptyList())

    private fun formatLabel(key: String, period: Period): String {
        return try {
            val parts = key.split("-")
            when (period) {
                Period.YEAR -> key
                Period.MONTH -> "${monthName(parts[1].toInt())} ${parts[0]}"
                Period.WEEK -> "Нед. ${parts[1]} (${parts[0]})"
            }
        } catch (e: Exception) { key }
    }

    private fun monthName(m: Int) = listOf("", "Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек")[m]
}