package com.example.cashlite.core.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Double.formatMoney(): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
        decimalSeparator = ','
    }

    return if (this@formatMoney % 1.0 == 0.0) {
        val df = DecimalFormat("#,###", symbols)
        df.format(this@formatMoney.toInt())
    } else {
        val df = DecimalFormat("#,###.00", symbols)
        df.format(this@formatMoney)
    }
}