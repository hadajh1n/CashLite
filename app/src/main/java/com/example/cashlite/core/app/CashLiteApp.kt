package com.example.cashlite.core.app

import android.app.Application
import android.util.Log
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader

class CashLiteApp : Application() {

    companion object {
        lateinit var instance: CashLiteApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this@CashLiteApp
        PDFBoxResourceLoader.init(this)
        Log.e("LOG_TESTING", "PDFBoxResourceLoader инициализирован")
    }
}