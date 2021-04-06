package ru.zabster.safepassapp.utils.dialogs

/**
 * Base data for dialog content
 *
 * @param title alert dialog title
 * @param message alert dialog message
 * @param isCancelable can dismiss when tap outside
 * @param isCancelable can select message text
 * @param positiveButtonText alert dialog text for positive button
 * @param negativeButtonText alert dialog text for negative button
 */
data class DialogDataModel(
    val title: String = "",
    val message: String = "",
    val isCancelable: Boolean = true,
    val isSelectable: Boolean = false,
    val positiveButtonText: String? = null,
    val negativeButtonText: String? = null
)
