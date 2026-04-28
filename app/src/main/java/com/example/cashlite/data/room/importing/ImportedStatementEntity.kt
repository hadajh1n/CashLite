package com.example.cashlite.data.room.importing

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imported_statements")
data class ImportedStatementEntity(
    @PrimaryKey
    val idImport: Int,
    val importTimeMillis: Long = System.currentTimeMillis()
)