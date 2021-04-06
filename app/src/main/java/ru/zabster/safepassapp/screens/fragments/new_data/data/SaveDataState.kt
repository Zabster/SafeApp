package ru.zabster.safepassapp.screens.fragments.new_data.data

/**
 * State for saving data screen
 */
sealed class SaveDataState {

    /**
     * State when saving data
     *
     * @param isComplete complete or not
     */
    data class SaveProcess(val isComplete: Boolean) : SaveDataState()

    /**
     * Validating fields
     *
     * @param validTitle have title
     * @param validPass have pass
     * @param validCategory select category
     */
    data class ValidateAll(val validTitle: Boolean, val validPass: Boolean, val validCategory: Boolean) :
        SaveDataState()

    /**
     * Validate category state
     */
    data class ValidateCategory(val validCategory: Boolean) : SaveDataState()
}
