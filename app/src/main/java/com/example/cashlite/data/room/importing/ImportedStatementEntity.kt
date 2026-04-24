package com.example.cashlite.data.room.importing

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imported_statements")
data class ImportedStatementEntity(
    @PrimaryKey
    val signature: String,
    val importTimeMillis: Long = System.currentTimeMillis()
)