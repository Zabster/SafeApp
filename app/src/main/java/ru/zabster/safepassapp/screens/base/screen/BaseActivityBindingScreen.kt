package ru.zabster.safepassapp.screens.base.screen

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import ru.zabster.safepassapp.app.App
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.utils.dialogs.loadingDialog

/**
 * Base class for Activity with DataBinding
 *
 * @param contentViewId layout res id
 */
abstract class BaseActivityBindingScreen<V : ViewDataBinding>(private val contentViewId: Int) : AppCompatActivity() {

    /**
     * ViewDataBinding with [contentViewId]
     */
    protected lateinit var binding: V
        private set

    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        initInject(App.getApplication().appComponents)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, contentViewId)
        binding.lifecycleOwner = this
        initAllDialogs()
    }

    override fun onStop() {
        hideLoadingDialog()
        super.onStop()
    }

    /**
     * Init Dagger
     *
     * @param components app components [AppComponents]
     */
    open fun initInject(components: AppComponents) {
    }

    /**
     * Show simple loading dialog
     */
    protected fun showLoadingDialog() {
        if (!loadingDialog.isShowing) loadingDialog.show()
    }

    /**
     * Hide simple loading dialog
     */
    protected fun hideLoadingDialog() {
        if (loadingDialog.isShowing) loadingDialog.dismiss()
    }

    private fun initAllDialogs() {
        loadingDialog = loadingDialog()
    }
}