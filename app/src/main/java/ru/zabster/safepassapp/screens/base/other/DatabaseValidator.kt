package ru.zabster.safepassapp.screens.base.other

/**
 * Data base creation data validator
 */
interface DatabaseValidator {

    /**
     * Call validate and repair
     *
     * This method check needed data and repair if needed
     */
    suspend fun validateAndRepair()

}