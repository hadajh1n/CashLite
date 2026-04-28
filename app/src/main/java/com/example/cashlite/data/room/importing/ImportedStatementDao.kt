package com.example.cashlite.data.room.importing

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImportedStatementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(statement: ImportedStatementEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM imported_statements WHERE idImport = :idImport)")
    suspend fun exists(idImport: Int): Boolean

    @Query("DELETE FROM imported_statements")
    suspend fun deleteAll()
}