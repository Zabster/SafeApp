package ru.zabster.safepassapp.screens.base.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

import ru.zabster.safepassapp.app.App
import ru.zabster.safepassapp.di.AppComponents

/**
 * Base class for Fragment with DataBinding
 *
 * @param contentViewId layout res id
 */
abstract class BaseFragmentBindingScreen<V : ViewDataBinding>(private val contentViewId: Int) : Fragment() {

    /**
     * ViewDataBinding with [contentViewId]
     *
     * This property is only valid between onCreateView and onDestroyView.
     */
    protected var binding: V? = null
        private set

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DataBindingUtil.inflate<V>(inflater, contentViewId, container, false).also { binding ->
            binding.lifecycleOwner = this
            this.binding = binding
            this.navController = findNavController()
        }.root

    override fun onCreate(savedInstanceState: Bundle?) {
        initInject(App.getApplication().appComponents)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Init Dagger
     *
     * @param components app components [AppComponents]
     */
    open fun initInject(components: AppComponents) {
    }

    /**
     * Using null safe view binding
     *
     * @param action some action with binding
     */
    open fun withFragmentBinding(action: V.() -> Unit) {
        binding?.let { fragmentBinding -> fragmentBinding.action() }
    }

    /**
     * Forward back
     */
    protected fun pop() {
        navController.popBackStack()
    }

    /**
     * Navigate to screen
     *
     * @param resId screen id
     */
    protected fun navigate(resId: Int) {
        navController.navigate(resId)
    }

    /**
     * Navigate to screen
     *
     * @param direction generated direction
     */
    protected fun navigate(direction: NavDirections) {
        navController.navigate(direction)
    }
}