package com.example.cashlite.ui.viewModel.newOperation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.core.PDFManager
import com.example.cashlite.core.app.CashLiteApp
import kotlinx.coroutines.launch

sealed class ImportUiState {

    object Loading: ImportUiState()
    object Standard: ImportUiState()
}

class AddOperationViewModel : ViewModel() {

    private val pdfManager = PDFManager(CashLiteApp.instance)

    private val _importState = MutableLiveData<ImportUiState>()
    val importState: LiveData<ImportUiState> = _importState

    private val _importSuccess = MutableLiveData<Boolean>()
    val importSuccess: LiveData<Boolean> = _importSuccess

    fun importPdf(uri: Uri) {
        viewModelScope.launch {
            try {
                _importState.value = ImportUiState.Loading
                val isSuccess = pdfManager.importPdf(uri)
                _importSuccess.value = isSuccess
                _importState.value = ImportUiState.Standard
            } catch (e: Exception) {
                e.printStackTrace()
                _importSuccess.value = false
                _importState.value = ImportUiState.Standard
            }
        }
    }
}