package com.example.cashlite.ui.viewModel.newOperation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashlite.core.PDFManager
import com.example.cashlite.core.app.CashLiteApp
import kotlinx.coroutines.launch

class AddOperationViewModel : ViewModel() {

    private val pdfManager = PDFManager(CashLiteApp.instance)

    private val _importSuccess = MutableLiveData<Boolean>()
    val importSuccess: LiveData<Boolean> = _importSuccess

    fun importPdf(uri: Uri) {
        Log.e("LOG_TESTING", "AddOperationViewModel: importPdf вызван")
        viewModelScope.launch {
            try {
                Log.e("LOG_TESTING", "Запускаем pdfManager.importPdf...")
                val isSuccess = pdfManager.importPdf(uri)   // ← теперь возвращает Boolean
                _importSuccess.value = isSuccess
                Log.e("LOG_TESTING", "importPdf завершён с результатом: $isSuccess")
            } catch (e: Exception) {
                Log.e("LOG_TESTING", "КРИТИЧЕСКАЯ ОШИБКА в importPdf: ${e.message}")
                e.printStackTrace()
                _importSuccess.value = false
            }
        }
    }
}