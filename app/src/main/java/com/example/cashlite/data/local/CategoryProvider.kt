package com.example.cashlite.data.local

import com.example.cashlite.R
import com.example.cashlite.data.dataclass.Transaction

object CategoryProvider {

    fun getExpenseCategories(): List<Transaction.Expense> {
        return listOf(
            Transaction.Expense(
                R.drawable.icon_expense_basket,
                R.color.icon_expense_basket,
                R.string.expense_supermarket
            ),
            Transaction.Expense(
                R.drawable.icon_expense_food,
                R.color.icon_expense_food,
                R.string.expense_food
            ),
            Transaction.Expense(
                R.drawable.icon_expense_clothes,
                R.color.icon_expense_clothes,
                R.string.expense_clothes
            ),
            Transaction.Expense(
                R.drawable.icon_expense_car,
                R.color.icon_expense_car,
                R.string.expense_car
            ),
            Transaction.Expense(
                R.drawable.icon_expense_bus,
                R.color.icon_expense_bus,
                R.string.expense_bus
            ),
            Transaction.Expense(
                R.drawable.icon_expense_bicycle,
                R.color.icon_expense_bicycle,
                R.string.expense_bicycle
            ),
            Transaction.Expense(
                R.drawable.icon_expense_housing,
                R.color.icon_expense_housing,
                R.string.expense_housing
            ),
            Transaction.Expense(
                R.drawable.icon_expense_education,
                R.color.icon_expense_education,
                R.string.expense_education
            ),
            Transaction.Expense(
                R.drawable.icon_expense_flag,
                R.color.icon_expense_flag,
                R.string.expense_flag
            ),
            Transaction.Expense(
                R.drawable.icon_expense_laptop,
                R.color.icon_expense_laptop,
                R.string.expense_electronics
            ),
            Transaction.Expense(
                R.drawable.icon_expense_phone,
                R.color.icon_expense_phone,
                R.string.expense_phone
            ),
            Transaction.Expense(
                R.drawable.icon_expense_pharmacy,
                R.color.icon_expense_pharmacy,
                R.string.expense_pharmacy
            ),
            Transaction.Expense(
                R.drawable.icon_expense_baby,
                R.color.icon_expense_baby,
                R.string.expense_baby
            ),
            Transaction.Expense(
                R.drawable.icon_expense_cat,
                R.color.icon_expense_cat,
                R.string.expense_cat
            ),
        )
    }

    fun getIncomeCategories(): List<Transaction.Income> {
        return listOf(
            Transaction.Income(
                R.drawable.icon_income_wallet,
                R.color.icon_income_wallet,
                R.string.income_wallet
            ),
            Transaction.Income(
                R.drawable.icon_income_graph,
                R.color.icon_income_graph,
                R.string.income_graph
            ),
            Transaction.Income(
                R.drawable.icon_income_award,
                R.color.icon_income_award,
                R.string.income_award
            ),
        )
    }

}