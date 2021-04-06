package ru.zabster.safepassapp.utils.bindings

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

/**
 * Set visible for view by [Boolean]
 */
@BindingAdapter(value = ["boolVisible"])
fun View?.boolVisible(isVisible: Boolean) {
    this?.isVisible = isVisible
}