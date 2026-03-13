package com.example.cashlite.core.utils

import java.text.DecimalFormat

fun Double.formatMoney(): String {
    return if (this@formatMoney % 1.0 == 0.0) {
        this@formatMoney.toInt().toString()
    } else {
        DecimalFormat("#0.00").format(this@formatMoney)
    }
}