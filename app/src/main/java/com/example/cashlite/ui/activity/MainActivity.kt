package com.example.cashlite.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.cashlite.R
import com.example.cashlite.core.manager.PreferenceManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val prefManager = PreferenceManager(applicationContext)

        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        val newMode = if (prefManager.isDarkMode)
            AppCompatDelegate.MODE_NIGHT_YES
        else
            AppCompatDelegate.MODE_NIGHT_NO

        if (currentNightMode != newMode) {
            AppCompatDelegate.setDefaultNightMode(newMode)
        }

        val currentLocales = AppCompatDelegate.getApplicationLocales()
        val newLocales = LocaleListCompat.forLanguageTags(prefManager.language)

        if (currentLocales != newLocales) {
            AppCompatDelegate.setApplicationLocales(newLocales)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}