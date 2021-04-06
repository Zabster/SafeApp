package ru.zabster.safepassapp.screens.main.presentation

import ru.zabster.safepassapp.di.MainComponents

/**
 * Main component manager for fragment
 */
interface MainComponentManager {

    /**
     * Access to main components for fragment
     */
    fun getMainComponent(): MainComponents
}