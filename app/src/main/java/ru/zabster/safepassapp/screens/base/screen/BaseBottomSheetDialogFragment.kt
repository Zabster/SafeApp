package ru.zabster.safepassapp.screens.base.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import ru.zabster.safepassapp.app.App
import ru.zabster.safepassapp.di.AppComponents

/**
 * Base class for BottomSheetDialogFragment with DataBinding
 *
 * @param layoutId layout res id
 */
abstract class BaseBottomSheetDialogFragment<V : ViewDataBinding>(private val layoutId: Int) : BottomSheetDialogFragment() {

    /**
     * ViewDataBinding with [layoutId]
     *
     * This property is only valid between onCreateView and onDestroyView.
     */
    protected var binding: V? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        DataBindingUtil.inflate<V>(inflater, layoutId, container, false).also { binding ->
            binding.lifecycleOwner = this
            this.binding = binding
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
}