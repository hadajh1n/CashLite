package com.example.cashlite.ui.viewModel.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import com.example.cashlite.core.manager.PreferenceManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefManager = PreferenceManager(application)

    fun isDarkMode() = prefManager.isDarkMode

    fun getCurrentLanguageCode() = prefManager.language

    fun toggleTheme(isEnabled: Boolean) {
        if (prefManager.isDarkMode == isEnabled) return

        prefManager.isDarkMode = isEnabled

        AppCompatDelegate.setDefaultNightMode(
            if (isEnabled)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun setLanguage(langCode: String) {
        if (prefManager.language == langCode) return

        prefManager.language = langCode

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(langCode)
        )
    }
}