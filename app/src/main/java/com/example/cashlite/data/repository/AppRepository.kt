package com.example.cashlite.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cashlite.R
import com.example.cashlite.data.dataclass.IconExpenseCategory
import com.example.cashlite.data.dataclass.NewExpenseTransaction

object AppRepository {

    private val transactionsList = mutableListOf<NewExpenseTransaction>()

    private val _transactions = MutableLiveData<List<NewExpenseTransaction>>()
    val transactions: LiveData<List<NewExpenseTransaction>> get() = _transactions

    fun getExpenseCategories(): List<IconExpenseCategory> {
        return listOf(
            IconExpenseCategory(R.drawable.icon_basket, "Супермаркет"),
            IconExpenseCategory(R.drawable.icon_food, "Еда"),
            IconExpenseCategory(R.drawable.icon_clothes, "Одежда"),
            IconExpenseCategory(R.drawable.icon_car, "Автомобиль"),
            IconExpenseCategory(R.drawable.icon_bicycle, "Спорт"),
            IconExpenseCategory(R.drawable.icon_education, "Образование"),
            IconExpenseCategory(R.drawable.icon_flag, "Путешествие"),
            IconExpenseCategory(R.drawable.icon_laptop, "Электроника"),
            IconExpenseCategory(R.drawable.icon_phone, "Телефон"),
            IconExpenseCategory(R.drawable.icon_pharmacy, "Аптека"),
        )
    }

    fun mapNewExpenseTransaction(
        category: IconExpenseCategory,
        amount: Double,
        note: String
    ) {
        val transaction = NewExpenseTransaction(
            category = category,
            amount = amount,
            note = note,
        )
        addTransactions(transaction)
    }

    private fun addTransactions(transaction: NewExpenseTransaction) {
        transactionsList.add(0, transaction)
        _transactions.value = transactionsList.toList()
    }

    fun getTransactions(): List<NewExpenseTransaction> {
        return transactionsList.toList()
    }
}