package ru.zabster.safepassapp.screens.base.data

/**
 * Category data for ui using
 *
 * @param id id from db
 * @param name category name
 * @param canDelete can be deleted or not
 * @param isSelect now is select or not
 */
data class CategoryData(
    val id: Long,
    val name: String,
    val canDelete: Boolean = false,
    var isSelect: Boolean = false
)
