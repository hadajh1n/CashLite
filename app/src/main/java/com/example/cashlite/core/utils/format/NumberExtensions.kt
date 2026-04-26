package com.example.cashlite.core.utils.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Double.formatMoney(): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault())
    val formatter = DecimalFormat("###,###,##0.00", symbols)

    return formatter.format(this)
}