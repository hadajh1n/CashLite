package com.example.cashlite.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun addExpenseTransaction(
        category: Transaction.Expense,
        amount: Double,
        note: String,
        date: String,
    ) {
        val transaction = Transaction.Expense(
            imageId = category.imageId,
            color = category.color,
            categoryNameRes = category.categoryNameRes,
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
            color = category.color,
            categoryNameRes = category.categoryNameRes,
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