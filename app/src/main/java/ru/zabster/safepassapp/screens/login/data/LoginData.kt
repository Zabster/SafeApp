package ru.zabster.safepassapp.screens.login.data

/**
 * UI data for login screen
 *
 * @param state screen state
 * @param error error type
 */
data class LoginData(
    val state: LoginState,
    val error: LoginErrorType
)
