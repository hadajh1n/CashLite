package com.example.cashlite.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.TotalsState
import com.example.cashlite.data.dataclass.Transaction

object AppRepository {

    private val transactionsList = mutableListOf<Transaction>()

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    private val _updateTotalTransactions = MutableLiveData<TotalsState>()
    val updateTotalTransactions: LiveData<TotalsState> get() = _updateTotalTransactions

    private var currentTotalExpense: Double = 0.0
    private var currentTotalIncome: Double = 0.0
    private var currentTotalBalance: Double = 0.0

    fun getExpenseCategories(): List<Transaction.Expense> {
        return listOf(
            Transaction.Expense(R.drawable.icon_expense_basket, "Супермаркет"),
            Transaction.Expense(R.drawable.icon_expense_food, "Еда"),
            Transaction.Expense(R.drawable.icon_expense_clothes, "Одежда"),
            Transaction.Expense(R.drawable.icon_expense_car, "Автомобиль"),
            Transaction.Expense(R.drawable.icon_expense_bus, "Транспорт"),
            Transaction.Expense(R.drawable.icon_expense_bicycle, "Спорт"),
            Transaction.Expense(R.drawable.icon_expense_housing, "Жилье"),
            Transaction.Expense(R.drawable.icon_expense_education, "Образование"),
            Transaction.Expense(R.drawable.icon_expense_flag, "Путешествие"),
            Transaction.Expense(R.drawable.icon_expense_laptop, "Электроника"),
            Transaction.Expense(R.drawable.icon_expense_phone, "Телефон"),
            Transaction.Expense(R.drawable.icon_expense_pharmacy, "Аптека"),
            Transaction.Expense(R.drawable.icon_expense_baby, "Детские"),
            Transaction.Expense(R.drawable.icon_expense_cat, "Домашний питомец"),
        )
    }

    fun getIncomeCategories(): List<Transaction.Income> {
        return listOf(
            Transaction.Income(R.drawable.icon_income_wallet, "Зарплата"),
            Transaction.Income(R.drawable.icon_income_graph, "Инвестиции"),
            Transaction.Income(R.drawable.icon_income_award, "Бонусы"),
        )
    }

    fun addExpenseTransaction(
        category: Transaction.Expense,
        amount: Double,
        note: String,
        date: String,
    ) {
        val transaction = Transaction.Expense(
            imageId = category.imageId,
            categoryName = category.categoryName,
            amount = amount,
            note = note,
            date = date,
        )
        addTransaction(transaction)
    }

    fun updateTotalExpense(amount: Double) {
        currentTotalExpense -= amount
    }

    fun addIncomeTransaction(
        category: Transaction.Income,
        amount: Double,
        note: String,
        date: String,
    ) {
        val transaction = Transaction.Income(
            imageId = category.imageId,
            categoryName = category.categoryName,
            amount = amount,
            note = note,
            date = date,
        )
        addTransaction(transaction)
    }

    fun updateTotalIncome(amount: Double) {
        currentTotalIncome += amount
    }

    fun updateTotals() {
        currentTotalBalance = currentTotalIncome + currentTotalExpense

        _updateTotalTransactions.value = TotalsState(
            totalExpense = currentTotalExpense,
            totalIncome = currentTotalIncome,
            totalBalance = currentTotalBalance
        )
    }

    private fun addTransaction(transaction: Transaction) {
        transactionsList.add(0, transaction)
        _transactions.value = transactionsList.toList()
    }

    fun removeTransaction(transaction: Transaction) {

        when (transaction) {
            is Transaction.Expense -> currentTotalExpense += transaction.amount
            is Transaction.Income -> currentTotalIncome -= transaction.amount
        }

        currentTotalBalance = currentTotalIncome + currentTotalExpense

        transactionsList.remove(transaction)
        _transactions.value = transactionsList.toList()

        _updateTotalTransactions.value = TotalsState(
            totalExpense = currentTotalExpense,
            totalIncome = currentTotalIncome,
            totalBalance = currentTotalBalance
        )
    }
}