package ru.zabster.safepassapp.di.impls.shared

import android.content.Context
import android.content.SharedPreferences

/**
 * Imp fo work with SharedPreferences
 *
 * @param context required to get an instance of SharedPreferences
 */
class SharedPreferenceImpl(context: Context) : SharedPreferenceHelper {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    override fun putString(key: String, value: String?, defaultValue: String) {
        sharedPreference.edit().putString(key, value ?: defaultValue).apply()
    }

    override fun getString(key: String, defaultValue: String): String =
        sharedPreference.getString(key, defaultValue) ?: defaultValue

    override fun putBoolean(key: String, value: Boolean?, defaultValue: Boolean) {
        sharedPreference.edit().putBoolean(key, value ?: defaultValue).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPreference.getBoolean(key, defaultValue)


    companion object {

        private const val SHARED_PREFERENCE_NAME = "spa_shared"
    }
}