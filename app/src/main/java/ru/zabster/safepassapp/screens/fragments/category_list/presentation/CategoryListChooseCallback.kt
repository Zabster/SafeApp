package ru.zabster.safepassapp.screens.fragments.category_list.presentation

import ru.zabster.safepassapp.screens.base.data.CategoryData

/**
 * Interface for chosen callback
 */
interface CategoryListChooseCallback {

    /**
     * Action choose category
     *
     * @param category simple category data
     */
    fun chooseCategory(category: CategoryData)
}