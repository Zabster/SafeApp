package ru.zabster.safepassapp.screens.login.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.zabster.safepassapp.di.impls.shared.SPKeys
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.screens.login.data.LoginData
import ru.zabster.safepassapp.screens.login.data.LoginErrorType
import ru.zabster.safepassapp.screens.login.data.LoginState
import ru.zabster.safepassapp.utils.secure.SecurityUtils

/**
 * Work with entered password
 *
 * @param sharedPreferenceHelper private local storage
 */
class PasswordInteractor(private val sharedPreferenceHelper: SharedPreferenceHelper) {

    /**
     * User try login with saved password
     *
     * @param password the password that the user entered
     *
     * @return [LoginData] login data for action
     */
    suspend fun checkLoginState(password: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): LoginData {
        return withContext(dispatcher) {
            if (password.isEmpty()) return@withContext LoginData(
                LoginState.Error,
                LoginErrorType.EMPTY_FIELD
            )
//            delay(2 * 1000)
            if (havePasswordInStorage()) {
                val secureKeyData = SecurityUtils.secureKeyDataFromGson(getPasswordFromStorage())
                    ?: return@withContext LoginData(LoginState.Error, LoginErrorType.SYSTEM_ERROR)

                val savedPassword = SecurityUtils.decryptSecret(secureKeyData, dispatcher)
                if (savedPassword.isEmpty()) return@withContext LoginData(
                    LoginState.Error,
                    LoginErrorType.SYSTEM_ERROR
                )

                return@withContext if (savedPassword == password) LoginData(LoginState.Success, LoginErrorType.NONE)
                else LoginData(LoginState.Error, LoginErrorType.INCORRECT_PASSWORD)
            } else {
                return@withContext LoginData(LoginState.Error, LoginErrorType.EMPTY_PASSWORD_IN_STORAGE)
            }
        }
    }

    /**
     * Set password to local storage
     *
     * @param password new password secure data
     *
     * @return [LoginData] login data for action
     */
    suspend fun setPassword(password: String): LoginData =
        withContext(Dispatchers.IO) {
            if (password.isEmpty()) return@withContext LoginData(
                LoginState.Error,
                LoginErrorType.EMPTY_FIELD
            )
            val isStored = SecurityUtils.getNewSecret(password)?.let { secureKeyData ->
                val jsonData = SecurityUtils.secureKeyDataToGson(secureKeyData)
                sharedPreferenceHelper.putString(SPKeys.KEY_DB_PASSWORD, jsonData)
                jsonData.isNotEmpty()
            } ?: false

            return@withContext if (isStored) LoginData(LoginState.Success, LoginErrorType.NONE)
            else LoginData(LoginState.Error, LoginErrorType.SYSTEM_ERROR)
        }

    private fun havePasswordInStorage(): Boolean = getPasswordFromStorage().isNotEmpty()

    private fun getPasswordFromStorage(): String = sharedPreferenceHelper.getString(SPKeys.KEY_DB_PASSWORD)
}