package ru.zabster.safepassapp.di.impls.shared

/**
 * Helper for work with SharedPreference
 */
interface SharedPreferenceHelper {

    /**
     * Save String value
     *
     * @param key key saved value
     * @param value string value
     * @param defaultValue default value, base is empty
     */
    fun putString(key: String, value: String?, defaultValue: String = "")

    /**
     * Get String value
     *
     * @param key key for searching value
     * @param defaultValue default value, base is empty
     *
     * @return value by key
     */
    fun getString(key: String, defaultValue: String = ""): String

    /**
     * Save Boolean value
     *
     * @param key key saved value
     * @param value boolean value
     * @param defaultValue default value, base is false
     */
    fun putBoolean(key: String, value: Boolean?, defaultValue: Boolean = false)

    /**
     * Get Boolean value
     *
     * @param key key saved value
     * @param defaultValue default value, base is false
     *
     * @return value by key
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

}