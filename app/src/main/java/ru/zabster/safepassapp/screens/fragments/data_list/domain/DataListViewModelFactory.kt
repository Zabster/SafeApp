package ru.zabster.safepassapp.screens.fragments.data_list.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import ru.zabster.safepassapp.db.AppDatabase
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager

/**
 * ViewModelFactory for DataListFragment
 *
 * @param mainComponents component for main screens
 * @param appComponents base app components
 */
class DataListViewModelFactory(
    private val mainComponents: MainComponents,
    private val appComponents: AppComponents
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(AppDatabase::class.java, ResourceManager::class.java)
            .newInstance(mainComponents.getDataBase().getSecureDataBase(), appComponents.getResourceManager())
}