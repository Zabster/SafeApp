package ru.zabster.safepassapp.screens.fragments.data_list.domain

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

import ru.zabster.safepassapp.screens.fragments.data_list.data.DataListScreenState
import ru.zabster.safepassapp.screens.fragments.data_list.data.DataListScreenState.EmptyList
import ru.zabster.safepassapp.screens.fragments.data_list.data.DataListScreenState.Loading
import ru.zabster.safepassapp.screens.fragments.data_list.data.DataListScreenState.NotEmptyList
import ru.zabster.safepassapp.screens.fragments.data_list.data.ViewType
import ru.zabster.safepassapp.screens.fragments.data_list.data.ViewType.CONTAINER
import ru.zabster.safepassapp.screens.fragments.data_list.data.ViewType.CONTENT
import ru.zabster.safepassapp.screens.fragments.data_list.data.ViewType.EMPTY
import ru.zabster.safepassapp.screens.fragments.data_list.data.ViewType.LOADING

/**
 * Set visible state for view by parameters
 *
 * @param state state DataListScreen
 * @param viewType view type
 */
@BindingAdapter(value = ["state", "viewType"], requireAll = true)
fun View?.bindState(state: DataListScreenState?, viewType: ViewType?) {
    if (this == null) return

    isVisible = when (state) {
        EmptyList -> viewType == EMPTY || viewType == CONTAINER
        Loading -> viewType == LOADING || viewType == CONTAINER
        NotEmptyList -> viewType == CONTENT
        else -> false
    }
}