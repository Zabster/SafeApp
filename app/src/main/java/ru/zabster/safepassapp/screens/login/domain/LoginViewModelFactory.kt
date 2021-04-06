package ru.zabster.safepassapp.screens.login.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager

import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper

/**
 * ViewModelFactory for LoginViewModel
 *
 * @param components app components
 */
class LoginViewModelFactory(
    private val components: AppComponents
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(ResourceManager::class.java, SharedPreferenceHelper::class.java)
            .newInstance(components.getResourceManager(), components.getSharedPreference())
}