package ru.zabster.safepassapp.screens.base.screen

import androidx.databinding.ViewDataBinding

import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.screens.main.presentation.MainComponentManager

/**
 * Base fragment for main screens
 *
 * @param contentViewId layout res id
 */
abstract class MainFragmentScreen<V : ViewDataBinding>(contentViewId: Int) :
    BaseFragmentBindingScreen<V>(contentViewId) {

    override fun initInject(components: AppComponents) {
        createViewModelFactory(
            components,
            (requireContext() as MainComponentManager).getMainComponent()
        )
    }

    /**
     * Access to components
     *
     * @param appComponents base application component
     * @param mainComponent component for main screens
     */
    open fun createViewModelFactory(appComponents: AppComponents, mainComponent: MainComponents) {
    }
}