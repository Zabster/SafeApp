package ru.zabster.safepassapp.screens.fragments.category_list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.zabster.safepassapp.db.dao.CategoryDao
import ru.zabster.safepassapp.db.entities.CategoryEntity
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.base.domain.DataListMapper

/**
 * Category list ViewModel
 *
 * @param categoryDao category access object
 */
class CategoryListViewModel(private val categoryDao: CategoryDao) : ViewModel() {

    /**
     * Category list
     */
    val categoryList: LiveData<List<CategoryData>>
        get() = Transformations.map(categoryDao.getCategoriesLiveData(), dataMapper::mapCategories)

    private val dataMapper = DataListMapper()

    /**
     * Add new category
     *
     * @param name category name
     */
    fun addCategory(name: String) {
        viewModelScope.launch {
            if (name.isNotEmpty()) {
                categoryDao.insertOrUpdate(CategoryEntity(name = name))
            }
        }
    }
}