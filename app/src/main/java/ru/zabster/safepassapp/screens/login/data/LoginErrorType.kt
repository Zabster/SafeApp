package ru.zabster.safepassapp.screens.login.data

/**
 * Types error for LoginScreen
 */
enum class LoginErrorType {
    /**
     * Don't have error message
     */
    NONE,

    /**
     * Error when password isn't valid
     */
    INCORRECT_PASSWORD,

    /**
     * Error when password from storage is empty
     */
    EMPTY_PASSWORD_IN_STORAGE,

    /**
     * Error when password field is empty
     */
    EMPTY_FIELD,

    /**
     * Other error with exceptions
     */
    SYSTEM_ERROR,
}