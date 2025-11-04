package com.example.cashlite.network.retrofit

import com.example.cashlite.data.dataclass.Transaction
import retrofit2.http.GET

interface TransactionApi {
    @GET("transactions")
    suspend fun getTransactions(): List<Transaction>
}