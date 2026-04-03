package com.example.cashlite.data.room.transaction

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cashlite.data.room.category.CategoryType

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE idTransaction = :idTransaction")
    suspend fun deleteById(idTransaction: Int)

    @Query("SELECT * FROM transactions ORDER BY idTransaction DESC")
    fun getAll(): LiveData<List<TransactionEntity>>

    @Query(
        "SELECT COALESCE(SUM(t.amount), 0.0) " +
        "FROM transactions t " +
        "JOIN categories c ON t.idCategory = c.idCategory " +
        "WHERE c.type = :type"
    )
    fun getSumByType(type: CategoryType): LiveData<Double>
}