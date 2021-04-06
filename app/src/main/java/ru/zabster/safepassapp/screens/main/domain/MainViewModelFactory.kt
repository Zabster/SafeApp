package ru.zabster.safepassapp.screens.main.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper

/**
 * Factory for MainViewModel
 *
 * @param components app components
 */
class MainViewModelFactory(
    private val components: AppComponents
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass
            .getConstructor(ResourceManager::class.java, SharedPreferenceHelper::class.java)
            .newInstance(components.getResourceManager(), components.getSharedPreference())
}