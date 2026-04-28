package com.example.cashlite.core.manager

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    companion object {
        private const val KEY_DARK_MODE = "key_dark_mode"
        private const val KEY_LANGUAGE = "key_language"
    }

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("cashlite_prefs", Context.MODE_PRIVATE)

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, true)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "ru") ?: "ru"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()
}