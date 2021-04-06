package ru.zabster.safepassapp.screens.fragments.settings.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.screens.fragments.settings.presentation.SettingsViewModel

/**
 * ViewModelFactory for [SettingsViewModel]
 *
 * @param appComponents base app component
 */
class SettingsViewModelFactory(
    private val appComponents: AppComponents
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(ResourceManager::class.java, SharedPreferenceHelper::class.java)
            .newInstance(appComponents.getResourceManager(), appComponents.getSharedPreference())
}