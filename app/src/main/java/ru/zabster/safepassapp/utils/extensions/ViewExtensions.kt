package ru.zabster.safepassapp.utils.extensions

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

/**
 * Set gone state for view
 */
fun View?.gone() {
    this?.isVisible = false
}

/**
 * Set visible state for view
 */
fun View?.visible() {
    this?.isVisible = true
}

/**
 * Set invisible state for view
 */
fun View?.invisible() {
    this?.isInvisible = true
}