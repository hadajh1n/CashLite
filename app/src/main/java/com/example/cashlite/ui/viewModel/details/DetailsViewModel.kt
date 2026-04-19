package com.example.cashlite.ui.viewModel.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.data.repository.AppRepository
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    fun onRemoveTransaction(idTransaction: Int) {
        viewModelScope.launch {
            AppRepository.removeTransaction(idTransaction)
        }
    }
}