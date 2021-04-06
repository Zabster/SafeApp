package ru.zabster.safepassapp.screens.main.domain

import android.content.Context
import androidx.room.migration.Migration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

import ru.zabster.safepassapp.di.DaggerMainComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.di.impls.shared.SPKeys
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.di.modules.DataBaseModule
import ru.zabster.safepassapp.utils.secure.SecurityUtils

/**
 * Main module creator
 *
 * @param sharedPreferenceHelper storage with password credentials
 */
class MainModuleCreator(
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val dispatcher: CoroutineDispatcher
) {

    /**
     * Try initialize [DaggerMainComponents]
     *
     * @return success or not
     */
    suspend fun initializeModule(context: Context): MainComponents? = withContext(dispatcher) {
        getPassword()?.let { password ->
            DaggerMainComponents
                .builder()
                .dataBaseModule(DataBaseModule(context, getMigrations(), password))
                .build()
        }
    }

    private fun getMigrations() = arrayOf<Migration>()

    private suspend fun getPassword(): String? = withContext(dispatcher) {
        SecurityUtils.secureKeyDataFromGson(
            sharedPreferenceHelper.getString(SPKeys.KEY_DB_PASSWORD)
        )?.let { secureData -> SecurityUtils.decryptSecret(secureData, dispatcher) }
    }
}