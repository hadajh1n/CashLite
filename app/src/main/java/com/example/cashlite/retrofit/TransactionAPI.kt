package com.example.cashlite.retrofit

import com.example.cashlite.Transaction
import retrofit2.http.GET

interface TransactionApi {
    @GET("transactions")
    suspend fun getTransactions(): List<Transaction>
}