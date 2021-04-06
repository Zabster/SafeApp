package ru.zabster.safepassapp.screens.main.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.Px
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.material.bottomappbar.BottomAppBar

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.databinding.ActivityMainBinding
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.di.annotations.MainScope
import ru.zabster.safepassapp.screens.base.screen.BaseActivityBindingScreen
import ru.zabster.safepassapp.screens.fragments.category_chip.presentation.CategoryChipBottomSheet
import ru.zabster.safepassapp.screens.login.presentation.LoginActivity
import ru.zabster.safepassapp.screens.main.data.MainScreenState
import ru.zabster.safepassapp.screens.main.data.MainScreenState.INITIALIZE_ERROR
import ru.zabster.safepassapp.screens.main.data.MainScreenState.INITIALIZE_SUCCESS
import ru.zabster.safepassapp.screens.main.domain.MainViewModelFactory
import ru.zabster.safepassapp.utils.dialogs.alert
import ru.zabster.safepassapp.utils.extensions.gone
import ru.zabster.safepassapp.utils.extensions.visible

/**
 * Main screen
 */
@MainScope
class MainActivity : BaseActivityBindingScreen<ActivityMainBinding>(R.layout.activity_main), MainComponentManager {

    private lateinit var viewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private lateinit var errorInitDialog: AlertDialog
    private var navController: NavController? = null
    private val destChangeListener = getDestinationChangeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgress()
        observeLiveData()
        initDialogs()
        initListeners()
    }

    override fun initInject(components: AppComponents) {
        viewModelFactory = MainViewModelFactory(components)
        setupMainComponent()
    }

    override fun getMainComponent(): MainComponents =
        viewModel.mainComponents ?: throw NullPointerException("Main component is null")

    override fun onResume() {
        super.onResume()
        if (!viewModel.isShow) { // initial state when loading content
            hideBottomBar()
        }
        binding.bottomAppBar.isVisible = viewModel.isShow // fix app bar visible after config change
        navController?.addOnDestinationChangedListener(destChangeListener)
    }

    override fun onPause() {
        navController?.removeOnDestinationChangedListener(destChangeListener)
        super.onPause()
    }

    private fun setupMainComponent() {
        viewModel.initMainComponent(this)
    }

    private fun initDialogs() {
        errorInitDialog = alert(viewModel.dialogDataInitializeError,
            positiveAction = {
                dismiss()
                LoginActivity.startLoginScreen(this@MainActivity)
            })
    }

    private fun observeLiveData() {
        viewModel.screenState.observe(this, this::handleMainScreenState)
    }

    private fun initListeners() {
        binding.buttonNew.setOnClickListener {
            navController?.navigate(R.id.createDataFragment)
        }
        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuSetting -> navController?.navigate(R.id.settingsFragment)
            }
            return@setOnMenuItemClickListener true
        }
        binding.bottomAppBar.setNavigationOnClickListener {
            CategoryChipBottomSheet.newInstance().show(supportFragmentManager, CATEGORY_FRAGMENT_TAG)
        }
    }

    private fun handleMainScreenState(state: MainScreenState) {
        when (state) {
            INITIALIZE_SUCCESS -> handleSuccess()
            INITIALIZE_ERROR -> showErrorInitDialog()
        }
    }

    private fun handleSuccess() {
        hideProgress()
        if (navController == null) {
            navController = binding.fragmentContainer.findNavController()
            viewModel.navState?.let { state ->
                navController?.restoreState(state)
            }
            navController?.setGraph(R.navigation.main_nav_graph)
            navController?.addOnDestinationChangedListener(destChangeListener)
        }
    }

    private fun getDestinationChangeListener(): NavController.OnDestinationChangedListener =
        NavController.OnDestinationChangedListener { navController: NavController, navDestination: NavDestination, _: Bundle? ->
            viewModel.navState = navController.saveState()
            when (navDestination.id) {
                R.id.dataListFragment -> showBottomBar()
                else -> hideBottomBar()
            }
        }

    private fun showBottomBar() {
        with(binding) {
            if (buttonNew.isOrWillBeHidden) {
                buttonNew.show()
                bottomAppBar.show()
                updateContainerMargins(bottom = resources.getDimensionPixelSize(R.dimen.action_bar_size))
            }
        }
    }

    private fun hideBottomBar() {
        with(binding) {
            if (buttonNew.isOrWillBeShown) {
                buttonNew.hide()
                bottomAppBar.hide()
                updateContainerMargins()
            }
        }
    }

    private fun showErrorInitDialog() {
        if (!errorInitDialog.isShowing) errorInitDialog.show()
    }

    private fun showProgress() {
        binding.progressBar.isVisible = true
        binding.fragmentContainer.isVisible = false
    }

    private fun hideProgress() {
        binding.progressBar.isVisible = false
        binding.fragmentContainer.isVisible = true
    }

    private fun ActivityMainBinding.updateContainerMargins(
        @Px left: Int = 0,
        @Px top: Int = 0,
        @Px right: Int = 0,
        @Px bottom: Int = 0
    ) {
        fragmentContainer.updateLayoutParams {
            (this as? CoordinatorLayout.LayoutParams)
                ?.updateMargins(left, top, right, bottom)
        }
    }

    private fun BottomAppBar.show() {
        if (!isVisible) {
            animate().translationY(ZERO_ALPHA).alpha(VISIBLE_ALPHA)
                .withStartAction {
                    visible()
                    viewModel.isShow = true
                }
        }
    }

    private fun BottomAppBar.hide() {
        if (isVisible) {
            val y = resources.getDimension(R.dimen.action_bar_size)
            animate().translationY(y).alpha(ZERO_ALPHA)
                .withEndAction {
                    gone()
                    viewModel.isShow = false
                }
        }
    }

    companion object {

        /**
         * Start new main screen with clear state
         */
        fun startNewMainScreen(context: Context) {
            context.startActivity(
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }

        private const val CATEGORY_FRAGMENT_TAG = "CategoryChipList"
        private const val ZERO_ALPHA = 0f
        private const val VISIBLE_ALPHA = 1f
    }
}