package ru.zabster.safepassapp.screens.fragments.category_list.domin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.zabster.safepassapp.db.dao.CategoryDao
import ru.zabster.safepassapp.di.MainComponents

/**
 * ViewModelFactory for CategoryListBottomSheet
 *
 * @param mainComponents component for main screens
 */
class CategoryListViewModelFactory(private val mainComponents: MainComponents) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(CategoryDao::class.java)
            .newInstance(mainComponents.getDataBase().getSecureDataBase().categoryDao())
}