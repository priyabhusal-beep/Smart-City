package com.example.smart_city.utils

import android.content.Context

object ThemePreference {

    private const val PREF_NAME = "theme_pref"
    private const val KEY_DARK_MODE = "dark_mode"

    fun saveDarkMode(context: Context, isDarkMode: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_MODE, isDarkMode)
            .apply()
    }

    fun getDarkMode(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_DARK_MODE, false)
    }
}