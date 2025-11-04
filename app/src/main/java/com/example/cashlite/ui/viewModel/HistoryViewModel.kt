package com.example.cashlite.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.data.dataclass.Transaction
import com.example.cashlite.network.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    fun loadTransactions() {
        viewModelScope.launch {
            try {
                val history = withContext(Dispatchers.IO) {
                    RetrofitClient.transactionApi.getTransactions()
                }
                Log.d("HistoryViewModel", "Получено ${history.size} транзакций")
                _transactions.postValue(history)
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Ошибка загрузки: ${e.message}", e)
            }
        }
    }
}
