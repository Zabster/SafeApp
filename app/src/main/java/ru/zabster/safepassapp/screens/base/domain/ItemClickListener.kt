package ru.zabster.safepassapp.screens.base.domain

/**
 * Click listener with any data
 */
interface ItemClickListener<T> {

    /**
     * On click callback
     *
     * @param item data for clicked item
     */
    fun onItemClick(item: T)
}