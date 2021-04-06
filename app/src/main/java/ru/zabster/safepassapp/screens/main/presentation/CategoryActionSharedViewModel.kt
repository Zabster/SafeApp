package ru.zabster.safepassapp.screens.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.zabster.safepassapp.screens.base.data.CategoryData

/**
 * Shared ViewModel for sharing categories actions
 */
class CategoryActionSharedViewModel : ViewModel() {

    /**
     * List of category
     */
    private val _categoryList = MutableLiveData<List<CategoryData>>()
    val categoryList: LiveData<List<CategoryData>>
        get() = _categoryList

    /**
     * Set check liveData category
     */
    private val _checkedCategoryLiveData = MutableLiveData<Pair<CategoryData, Boolean>>()
    val checkedCategoryLiveData: LiveData<Pair<CategoryData, Boolean>>
        get() = _checkedCategoryLiveData

    /**
     * Deleting liveData category
     */
    private val _deleteCategoryLiveData = MutableLiveData<CategoryData>()
    val deleteCategoryLiveData: LiveData<CategoryData>
        get() = _deleteCategoryLiveData

    /**
     * Update category list
     *
     * @param list list of [CategoryData]
     */
    fun setCategoryList(list: List<CategoryData>) {
        _categoryList.postValue(list)
    }

    /**
     * Select category
     *
     * @param category selected category data
     * @param isSelect select or not
     */
    fun selectCategory(category: CategoryData, isSelect: Boolean) {
        _checkedCategoryLiveData.value = Pair(category, isSelect)
    }

    /**
     * Delete category
     *
     * @param category category data for deleting
     */
    fun deleteCategory(category: CategoryData) {
        _deleteCategoryLiveData.postValue(category)
    }
}