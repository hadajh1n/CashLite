package com.example.cashlite.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.example.cashlite.core.app.CashLiteApp
import com.example.cashlite.data.dataclass.history.CategoryUI
import com.example.cashlite.data.dataclass.history.TransactionUI
import com.example.cashlite.data.local.CategoryKeys
import com.example.cashlite.data.mapper.CategoryEntityMapper
import com.example.cashlite.data.mapper.TransactionEntityMapper
import com.example.cashlite.data.room.database.AppDatabase
import com.example.cashlite.data.room.category.CategoryType
import com.example.cashlite.data.room.importing.ImportedStatementEntity
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
    private val importedStatementDao = db.importedStatementDao()

    val expenseCategories: LiveData<List<CategoryUI>> = MediatorLiveData<List<CategoryUI>>().apply {
        val source = categoryDao.getCategoriesByType(CategoryType.EXPENSE)
        addSource(source) { categories ->
            value = categories
                .filter {
                    it.categoryName !in CategoryKeys.TRANSFER_CATEGORIES &&
                            it.categoryName != CategoryKeys.UNKNOWN_EXPENSE &&
                            it.categoryName != CategoryKeys.UNKNOWN_INCOME
                }
                .map { categoryUiMapper.fromEntityToUI(it) }
        }
    }

    val incomeCategories: LiveData<List<CategoryUI>> = MediatorLiveData<List<CategoryUI>>().apply {
        val source = categoryDao.getCategoriesByType(CategoryType.INCOME)
        addSource(source) { categories ->
            value = categories
                .filter {
                    it.categoryName !in CategoryKeys.TRANSFER_CATEGORIES &&
                            it.categoryName != CategoryKeys.UNKNOWN_EXPENSE &&
                            it.categoryName != CategoryKeys.UNKNOWN_INCOME
                }
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

    suspend fun initCategories() {
        if (categoryDao.getCount() == 0) {
            categoryDao.insertAll(categoryEntityMapper.getAllSystemCategories())
        }
    }

    suspend fun getCategoryByName(categoryName: String): CategoryUI? {
        val entity = categoryDao.getByName(categoryName) ?: return null
        return categoryUiMapper.fromEntityToUI(entity)
    }

    fun generateTransactionId(date: Long, amount: Double, note: String): Int {
        return "${date}_${amount}_${note}".hashCode()
    }

    suspend fun isTransactionDuplicate(idImport: Int): Boolean =
        importedStatementDao.exists(idImport)

    suspend fun insertImportedTransactions(
        transactions: List<TransactionEntity>,
        idImport: List<Int>
    ) {
        db.withTransaction {
            idImport.forEach { id ->
                importedStatementDao.insert(ImportedStatementEntity(id))
            }
            transactionDao.insertAllImport(transactions)
        }
    }

    suspend fun hasImportedTransactions(): Boolean {
        return transactionDao.hasImportedTransactions()
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

    suspend fun deleteAllTransactions() {
        db.withTransaction {
            transactionDao.deleteAll()
            importedStatementDao.deleteAll()
        }
    }

    suspend fun deleteImportedTransactions() {
        db.withTransaction {
            transactionDao.deleteAllImported()
            importedStatementDao.deleteAll()
        }
    }
}