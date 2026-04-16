package com.example.cashlite.data.local

import com.example.cashlite.R

object CategoryKeys {

    const val EXPENSE_SUPERMARKET = "expense_supermarket"
    const val EXPENSE_FOOD = "expense_food"
    const val EXPENSE_CLOTHES = "expense_clothes"
    const val EXPENSE_CAR = "expense_car"
    const val EXPENSE_BUS = "expense_bus"
    const val EXPENSE_BICYCLE = "expense_bicycle"
    const val EXPENSE_HOUSING = "expense_housing"
    const val EXPENSE_MARKETPLACE = "expense_marketplace"
    const val EXPENSE_TAXI = "expense_taxi"
    const val EXPENSE_EDUCATION = "expense_education"
    const val EXPENSE_FLAG = "expense_flag"
    const val EXPENSE_ELECTRONICS = "expense_electronics"
    const val EXPENSE_PHONE = "expense_phone"
    const val EXPENSE_PHARMACY = "expense_pharmacy"
    const val EXPENSE_BABY = "expense_baby"
    const val EXPENSE_CAT = "expense_cat"

    const val INCOME_WALLET = "income_wallet"
    const val INCOME_CASHBACK = "income_cashback"
    const val INCOME_GRAPH = "income_graph"
    const val INCOME_AWARD = "income_award"

    const val TRANSFER_EXPENSE = "transfer_expense"
    const val TRANSFER_INCOME = "transfer_income"

    const val UNKNOWN_EXPENSE = "unknown_expense"
    const val UNKNOWN_INCOME = "unknown_income"

    val TRANSFER_CATEGORIES = listOf(TRANSFER_EXPENSE, TRANSFER_INCOME)

    fun getCategoryNameRes(key: String): Int = when (key) {
        EXPENSE_SUPERMARKET -> R.string.expense_supermarket
        EXPENSE_FOOD -> R.string.expense_food
        EXPENSE_CLOTHES -> R.string.expense_clothes
        EXPENSE_CAR -> R.string.expense_car
        EXPENSE_BUS -> R.string.expense_bus
        EXPENSE_BICYCLE -> R.string.expense_bicycle
        EXPENSE_HOUSING -> R.string.expense_housing
        EXPENSE_MARKETPLACE -> R.string.expense_marketplace
        EXPENSE_TAXI -> R.string.expense_taxi
        EXPENSE_EDUCATION -> R.string.expense_education
        EXPENSE_FLAG -> R.string.expense_flag
        EXPENSE_ELECTRONICS -> R.string.expense_electronics
        EXPENSE_PHONE -> R.string.expense_phone
        EXPENSE_PHARMACY -> R.string.expense_pharmacy
        EXPENSE_BABY -> R.string.expense_baby
        EXPENSE_CAT -> R.string.expense_cat

        INCOME_WALLET -> R.string.income_wallet
        INCOME_CASHBACK -> R.string.income_cashback
        INCOME_GRAPH -> R.string.income_graph
        INCOME_AWARD -> R.string.income_award

        TRANSFER_EXPENSE -> R.string.transfer_expense
        TRANSFER_INCOME -> R.string.transfer_income

        else -> R.string.unknown_general
    }
}