package com.example.cashlite.ui.viewModel.main

import androidx.lifecycle.*
import com.example.cashlite.R
import com.example.cashlite.core.app.CashLiteApp
import com.example.cashlite.core.utils.format.formatDate
import com.example.cashlite.data.dataclass.history.HistoryFilter
import com.example.cashlite.data.dataclass.history.HistoryItem
import com.example.cashlite.data.dataclass.history.TotalsStateUI
import com.example.cashlite.data.dataclass.history.TransactionUI
import com.example.cashlite.data.repository.AppRepository
import com.example.cashlite.data.room.category.CategoryType
import kotlinx.coroutines.launch
import java.util.Calendar

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    object Empty : HistoryUiState()
    data class Content(val items: List<HistoryItem>) : HistoryUiState()
}

class HistoryViewModel : ViewModel() {

    private val transactions: LiveData<List<TransactionUI>> = AppRepository.transactions

    private val _filter = MutableLiveData<HistoryFilter>().apply {
        val cal = Calendar.getInstance()
        value = HistoryFilter(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false)
    }
    val filter: LiveData<HistoryFilter> = _filter

    val monthYearLabel: LiveData<HistoryFilter> = _filter

    fun refresh() {
        _filter.value = _filter.value
    }

    private val filteredTransactions = MediatorLiveData<List<TransactionUI>>().apply {
        fun update() {
            val list = transactions.value ?: emptyList()
            val f = _filter.value ?: return

            value = if (f.isAllTime) {
                list
            } else {
                list.filter {
                    val cal = Calendar.getInstance().apply { timeInMillis = it.date }
                    cal.get(Calendar.MONTH) == f.month && cal.get(Calendar.YEAR) == f.year
                }
            }
        }
        addSource(transactions) { update() }
        addSource(_filter) { update() }
    }

    val filteredTotals: LiveData<TotalsStateUI> = filteredTransactions.map { list ->
        val expense = list.filter { it.type == CategoryType.EXPENSE }.sumOf { it.amount }
        val income = list.filter { it.type == CategoryType.INCOME }.sumOf { it.amount }
        TotalsStateUI(expense, income, income - expense)
    }

    val uiHistoryState: LiveData<HistoryUiState> = filteredTransactions.map { list ->
        if (list.isEmpty()) HistoryUiState.Empty
        else HistoryUiState.Content(buildHistoryItems(list))
    }

    fun setFilter(month: Int, year: Int) {
        _filter.value = HistoryFilter(month, year, false)
    }

    fun setAllTimeFilter() {
        _filter.value = HistoryFilter(0, 0, true)
    }

    private fun buildHistoryItems(transactions: List<TransactionUI>): List<HistoryItem> {
        val sorted = transactions.sortedByDescending { it.date }
        val grouped = sorted.groupBy { formatDate(it.date) }
        val result = mutableListOf<HistoryItem>()
        for ((date, list) in grouped) {
            result.add(HistoryItem.DateHeader(date))
            list.forEach { result.add(HistoryItem.TransactionItem(it)) }
        }
        return result
    }

    fun resetToCurrentDate() {
        val cal = Calendar.getInstance()
        _filter.value = HistoryFilter(
            cal.get(Calendar.MONTH),
            cal.get(Calendar.YEAR),
            false
        )
    }

    fun onSwipeRemoveTransaction(transaction: TransactionUI) {
        viewModelScope.launch { AppRepository.removeTransaction(transaction.idTransaction) }
    }

    fun onDeleteAllTransactions() {
        viewModelScope.launch { AppRepository.deleteAllTransactions() }
    }

    fun onDeleteImportedTransactions() {
        viewModelScope.launch { AppRepository.deleteImportedTransactions() }
    }
}