package com.example.cashlite.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.room.Room
import com.example.cashlite.core.app.CashLiteApp
import com.example.cashlite.data.dataclass.CategoryUI
import com.example.cashlite.data.dataclass.TotalsStateUI
import com.example.cashlite.data.dataclass.TransactionUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.mapper.CategoryEntityMapper
import com.example.cashlite.data.mapper.TransactionEntityMapper
import com.example.cashlite.data.room.AppDatabase
import com.example.cashlite.data.room.category.CategoryType
import com.example.cashlite.data.room.transaction.TransactionEntity
import com.example.cashlite.ui.mapper.CategoryUiMapper
import com.example.cashlite.ui.mapper.TransactionUiMapper

object AppRepository {

    private val categoryEntityMapper = CategoryEntityMapper()
    private val transactionEntityMapper = TransactionEntityMapper()
    private val categoryUiMapper = CategoryUiMapper()
    private val transactionUiMapper = TransactionUiMapper()

    private val db by lazy {
        Room.databaseBuilder(
            CashLiteApp.instance.applicationContext,
            AppDatabase::class.java,
            "cash-lite-db"
        ).build()
    }

    private val categoryDao = db.categoryDao()
    private val transactionDao = db.transactionDao()

    val expenseCategories: LiveData<List<CategoryUI>> = MediatorLiveData<List<CategoryUI>>().apply {
        val source = categoryDao.getCategoriesByType(CategoryType.EXPENSE)
        addSource(source) { categories ->
            value = categories
                .filter { it.categoryName !in CategoryKeys.TRANSFER_CATEGORIES }
                .map { categoryUiMapper.fromEntityToUI(it) }
        }
    }

    val incomeCategories: LiveData<List<CategoryUI>> = MediatorLiveData<List<CategoryUI>>().apply {
        val source = categoryDao.getCategoriesByType(CategoryType.INCOME)
        addSource(source) { categories ->
            value = categories
                .filter { it.categoryName !in CategoryKeys.TRANSFER_CATEGORIES }
                .map { categoryUiMapper.fromEntityToUI(it) }
        }
    }

    val transactions: LiveData<List<TransactionUI>> = MediatorLiveData<List<TransactionUI>>().apply {
        val transactionsSource = transactionDao.getAll()
        val categoriesSource = categoryDao.getAllCategories()

        fun updateTransactions() {
            val transList = transactionsSource.value ?: return
            val catList = categoriesSource.value ?: return

            val catMap = catList.associateBy { it.idCategory }
            value = transList.mapNotNull { transEntity ->
                val catEntity = catMap[transEntity.idCategory] ?: return@mapNotNull null
                transactionUiMapper.fromEntityToUI(transEntity, catEntity)
            }
        }

        addSource(transactionsSource) { updateTransactions() }
        addSource(categoriesSource) { updateTransactions() }
    }

    val totalTransaction: LiveData<TotalsStateUI> = MediatorLiveData<TotalsStateUI>().apply {
        val expenseLive = transactionDao.getSumByType(CategoryType.EXPENSE)
        val incomeLive = transactionDao.getSumByType(CategoryType.INCOME)

        fun updateTotals() {
            val expenseSum = expenseLive.value ?: 0.0
            val incomeSum = incomeLive.value ?: 0.0
            value = calculateTotals(expenseSum, incomeSum)
        }

        addSource(expenseLive) { updateTotals() }
        addSource(incomeLive) { updateTotals() }
    }

    private fun calculateTotals(expenseSum: Double, incomeSum: Double): TotalsStateUI {
        return TotalsStateUI(
            totalExpense = expenseSum,
            totalIncome = incomeSum,
            totalBalance = incomeSum - expenseSum
        )
    }

    suspend fun initCategories() {
        if (categoryDao.getCount() == 0) {
            categoryDao.insertAll(categoryEntityMapper.getAllSystemCategories())
        }
    }

    suspend fun getCategoryByName(categoryName: String): CategoryUI? {
        val entity = categoryDao.getByName(categoryName) ?: return null
        return categoryUiMapper.fromEntityToUI(entity)
    }

    suspend fun insertImportedTransactions(transactions: List<TransactionEntity>) {
        transactionDao.insertAllImport(transactions)
    }

    suspend fun addTransaction(
        category: CategoryUI,
        amount: Double,
        note: String,
        date: Long,
        contact: String? = null,
    ) {
        val entity = transactionEntityMapper
            .transactionToEntity(category, amount, note, date, contact)
        transactionDao.insert(entity)
    }

    suspend fun removeTransaction(idTransaction: Int) {
        transactionDao.deleteById(idTransaction)
    }
}