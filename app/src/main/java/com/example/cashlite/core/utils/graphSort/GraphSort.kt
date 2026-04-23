package com.example.cashlite.core.utils.graphSort

import java.util.Calendar

fun Long.year(): Int {
    return Calendar.getInstance().apply {
        timeInMillis = this@year
    }.get(Calendar.YEAR)
}

fun Long.yearMonth(): String {
    val cal = Calendar.getInstance().apply {
        timeInMillis = this@yearMonth
    }
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH) + 1

    return "$y-${m.toString().padStart(2, '0')}"
}

fun Long.weekOfYearKey(): String {
    val cal = Calendar.getInstance().apply {
        timeInMillis = this@weekOfYearKey
    }
    val y = cal.get(Calendar.YEAR)
    val w = cal.get(Calendar.WEEK_OF_YEAR)

    return "$y-${w.toString().padStart(2, '0')}"
}