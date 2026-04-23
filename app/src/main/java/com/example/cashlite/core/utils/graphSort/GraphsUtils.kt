package com.example.cashlite.core.utils.graphSort

import com.example.cashlite.data.dataclass.graphs.GraphPeriodUI
import com.example.cashlite.data.dataclass.graphs.Period
import java.util.Calendar

object GraphUtils {

    private const val MAX_PERIODS_LIMIT = 100

    fun getCurrentPeriodKey(period: Period): String =
        getPeriodKey(System.currentTimeMillis(), period)

    fun getPeriodKey(date: Long, period: Period): String {
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

    fun generateContinuousPeriods(earliestDate: Long, period: Period): List<GraphPeriodUI> {
        val currentCal = Calendar.getInstance()
        val earliestCal = Calendar.getInstance().apply { timeInMillis = earliestDate }

        currentCal.set(Calendar.HOUR_OF_DAY, 0)
        earliestCal.set(Calendar.HOUR_OF_DAY, 0)

        val resultKeys = mutableListOf<String>()
        val tempCal = Calendar.getInstance().apply { timeInMillis = currentCal.timeInMillis }

        while (tempCal.after(earliestCal) || isSamePeriod(tempCal, earliestCal, period)) {
            val key = getPeriodKey(tempCal.timeInMillis, period)
            if (!resultKeys.contains(key)) {
                resultKeys.add(key)
            }
            when (period) {
                Period.WEEK -> tempCal.add(Calendar.WEEK_OF_YEAR, -1)
                Period.MONTH -> tempCal.add(Calendar.MONTH, -1)
                Period.YEAR -> tempCal.add(Calendar.YEAR, -1)
            }
            if (resultKeys.size > MAX_PERIODS_LIMIT) break
        }

        return resultKeys.reversed().map {
            GraphPeriodUI(key = it, label = formatLabel(it, period))
        }
    }

    private fun isSamePeriod(c1: Calendar, c2: Calendar, period: Period): Boolean {
        return when (period) {
            Period.YEAR -> c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            Period.MONTH -> c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                    c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
            Period.WEEK -> c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                    c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR)
        }
    }

    fun formatLabel(key: String, period: Period): String {
        return try {
            val parts = key.split("-")
            when (period) {
                Period.YEAR -> parts[0]
                Period.MONTH -> "${monthName(parts[1].toInt())} ${parts[0]}"
                Period.WEEK -> {
                    val year = parts[0].toInt()
                    val week = parts[1].toInt()
                    getWeekRangeLabel(year, week)
                }
            }
        } catch (e: Exception) { key }
    }

    private fun getWeekRangeLabel(year: Int, week: Int): String {
        val cal = Calendar.getInstance()
        cal.clear()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.WEEK_OF_YEAR, week)

        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)

        val startDay = cal.get(Calendar.DAY_OF_MONTH)
        val startMonth = cal.get(Calendar.MONTH) + 1
        val startYear = cal.get(Calendar.YEAR)

        cal.add(Calendar.DAY_OF_YEAR, 6)

        val endDay = cal.get(Calendar.DAY_OF_MONTH)
        val endMonth = cal.get(Calendar.MONTH) + 1
        val endYear = cal.get(Calendar.YEAR)

        return when {
            startYear == endYear && startMonth == endMonth -> {
                "$startDay-$endDay ${monthName(startMonth)} $startYear"
            }

            startYear == endYear -> {
                "$startDay ${monthName(startMonth)} - $endDay ${monthName(endMonth)} $startYear"
            }

            else -> {
                "$startDay ${monthName(startMonth)} $startYear - " +
                        "$endDay ${monthName(endMonth)} $endYear"
            }
        }
    }

    private fun monthName(m: Int) = listOf(
        "", "Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
    )[m]
}