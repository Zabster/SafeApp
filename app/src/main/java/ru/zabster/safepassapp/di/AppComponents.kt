package ru.zabster.safepassapp.di

import dagger.Component
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager
import ru.zabster.safepassapp.di.impls.room.RoomHelper
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.di.modules.DataBaseModule
import ru.zabster.safepassapp.di.modules.ResourceModule
import ru.zabster.safepassapp.di.modules.SharedPreferenceModule
import javax.inject.Singleton

/**
 * Base app component
 */
@Singleton
@Component(modules = [SharedPreferenceModule::class, ResourceModule::class])
interface AppComponents {

    /**
     * SharedPreference instance
     */
    fun getSharedPreference(): SharedPreferenceHelper

    /**
     * ResourceManager instance
     */
    fun getResourceManager(): ResourceManager
}