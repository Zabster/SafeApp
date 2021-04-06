package ru.zabster.safepassapp.di.impls.rescontents

import android.content.Context

/**
 * Implementation for [ResourceManager]
 *
 * @param context for access to the app resources
 */
class ResourceManagerImpl(private val context: Context) : ResourceManager {

    override fun getString(resStringId: Int): String = context.getString(resStringId)
}