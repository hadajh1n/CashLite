package com.example.cashlite.core.app

import android.app.Application
import android.util.Log
import com.example.cashlite.data.repository.AppRepository
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CashLiteApp : Application() {

    companion object {
        lateinit var instance: CashLiteApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this@CashLiteApp
        PDFBoxResourceLoader.init(this)
        Log.e("LOG_TESTING", "PDFBoxResourceLoader инициализирован")
        initSystemCategoriesOnce()
    }

    private fun initSystemCategoriesOnce() {
        CoroutineScope(Dispatchers.IO).launch {
            AppRepository.initCategories()
            Log.e("TEST_WORKING", "Системные категории инициализированы при старте приложения")
        }
    }
}