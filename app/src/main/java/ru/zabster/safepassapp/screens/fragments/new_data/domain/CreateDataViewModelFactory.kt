package ru.zabster.safepassapp.screens.fragments.new_data.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import ru.zabster.safepassapp.db.AppDatabase
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager

/**
 * ViewModelFactory for CreateDataFragment
 *
 * @param appComponents app components
 * @param mainComponents component for main screens
 */
class CreateDataViewModelFactory(
    private val appComponents: AppComponents,
    private val mainComponents: MainComponents
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(AppDatabase::class.java, ResourceManager::class.java)
            .newInstance(mainComponents.getDataBase().getSecureDataBase(), appComponents.getResourceManager())
}