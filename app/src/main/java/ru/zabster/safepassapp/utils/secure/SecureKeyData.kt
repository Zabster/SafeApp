package ru.zabster.safepassapp.utils.secure

/**
 * Model for security key in storage
 *
 * @param key encrypt key
 * @param iv initialization vector
 * @param salt cryptographic salt
 */
data class SecureKeyData(
    val key: String,
    val iv: String,
    val salt: String
)
