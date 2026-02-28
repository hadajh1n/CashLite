package com.example.cashlite.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.IconExpenseCategory
import com.example.cashlite.data.dataclass.IconIncomeCategory
import com.example.cashlite.data.dataclass.NewExpenseTransaction
import com.example.cashlite.data.dataclass.NewIncomeTransaction
import com.example.cashlite.data.dataclass.Transaction

object AppRepository {

    private val transactionsList = mutableListOf<Transaction>()

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

//    private val transactionsExpenseList = mutableListOf<NewExpenseTransaction>()
//    private val transactionsIncomeList = mutableListOf<NewIncomeTransaction>()
//
//    private val _transactionsExpense = MutableLiveData<List<NewExpenseTransaction>>()
//    val transactionsExpense: LiveData<List<NewExpenseTransaction>> get() = _transactionsExpense
//
//    private val _transactionsIncome = MutableLiveData<List<NewIncomeTransaction>>()
//    val transactionsIncome: LiveData<List<NewIncomeTransaction>> get() = _transactionsIncome

    fun getExpenseCategories(): List<IconExpenseCategory> {
        return listOf(
            IconExpenseCategory(R.drawable.icon_expense_basket, "Супермаркет"),
            IconExpenseCategory(R.drawable.icon_expense_food, "Еда"),
            IconExpenseCategory(R.drawable.icon_expense_clothes, "Одежда"),
            IconExpenseCategory(R.drawable.icon_expense_car, "Автомобиль"),
            IconExpenseCategory(R.drawable.icon_expense_bus, "Транспорт"),
            IconExpenseCategory(R.drawable.icon_expense_bicycle, "Спорт"),
            IconExpenseCategory(R.drawable.icon_expense_housing, "Жилье"),
            IconExpenseCategory(R.drawable.icon_expense_education, "Образование"),
            IconExpenseCategory(R.drawable.icon_expense_flag, "Путешествие"),
            IconExpenseCategory(R.drawable.icon_expense_laptop, "Электроника"),
            IconExpenseCategory(R.drawable.icon_expense_phone, "Телефон"),
            IconExpenseCategory(R.drawable.icon_expense_pharmacy, "Аптека"),
            IconExpenseCategory(R.drawable.icon_expense_baby, "Детские"),
            IconExpenseCategory(R.drawable.icon_expense_cat, "Домашний питомец"),
        )
    }

    fun getIncomeCategories(): List<IconIncomeCategory> {
        return listOf(
            IconIncomeCategory(R.drawable.icon_income_wallet, "Зарплата"),
            IconIncomeCategory(R.drawable.icon_income_graph, "Инвестиции"),
            IconIncomeCategory(R.drawable.icon_income_award, "Бонусы"),
        )
    }

    fun addExpenseTransaction(
        category: IconExpenseCategory,
        amount: Double,
        note: String
    ) {
        val transaction = Transaction.Expense(
            imageId = category.imageId,
            categoryName = category.categoryName,
            amount = amount,
            note = note,
        )
        addTransaction(transaction)
    }

    fun addIncomeTransaction(
        category: IconIncomeCategory,
        amount: Double,
        note: String
    ) {
        val transaction = Transaction.Income(
            imageId = category.imageId,
            categoryName = category.categoryName,
            amount = amount,
            note = note,
        )
        addTransaction(transaction)
    }

    private fun addTransaction(transaction: Transaction) {
        transactionsList.add(0, transaction)
        _transactions.value = transactionsList.toList()
    }

//    private fun addExpenseTransactions(transaction: NewExpenseTransaction) {
//        transactionsExpenseList.add(0, transaction)
//        _transactionsExpense.value = transactionsExpenseList.toList()
//    }
//
//    private fun addIncomeTransactions(transaction: NewIncomeTransaction) {
//        transactionsIncomeList.add(0, transaction)
//        _transactionsIncome.value = transactionsIncomeList.toList()
//    }
}