package ru.zabster.safepassapp.utils.theme

import androidx.appcompat.app.AppCompatDelegate

/**
 * Theme repository
 */
object AppTheme {

    /**
     * Update theme state
     *
     * @param isDark use dark or light
     */
    fun updateTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}