package com.example.cashlite.core.app

import android.app.Application

class CashLiteApp : Application() {

    companion object {
        lateinit var instance: CashLiteApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this@CashLiteApp
    }
}