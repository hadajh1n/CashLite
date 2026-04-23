package com.example.cashlite.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cashlite.data.room.category.CategoryDao
import com.example.cashlite.data.room.category.CategoryEntity
import com.example.cashlite.data.room.transaction.TransactionDao
import com.example.cashlite.data.room.transaction.TransactionEntity

@Database(
    entities = [TransactionEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}