package ru.zabster.safepassapp.di.impls.rescontents

/**
 * Resource manger
 */
interface ResourceManager {

    /**
     * Get string resource
     *
     * @param resStringId resource id (R.string...)
     *
     * @return string value by id
     */
    fun getString(resStringId: Int): String

}