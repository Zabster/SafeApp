package ru.zabster.safepassapp.screens.fragments.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.databinding.FragmentSettingsBinding
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.screens.base.screen.BaseFragmentBindingScreen
import ru.zabster.safepassapp.screens.fragments.settings.domain.SettingsViewModelFactory
import ru.zabster.safepassapp.screens.login.presentation.LoginActivity
import ru.zabster.safepassapp.utils.dialogs.alert

/**
 * Settings screen
 */
class SettingsFragment : BaseFragmentBindingScreen<FragmentSettingsBinding>(R.layout.fragment_settings) {

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initViewModel()
    }

    private fun initViewModel() {
        withFragmentBinding {
            vm = viewModel
        }
    }

    override fun initInject(components: AppComponents) {
        viewModelFactory = SettingsViewModelFactory(components)
    }

    private fun initListeners() {
        withFragmentBinding {
            materialToolbar.setNavigationOnClickListener { pop() }
            logoutBtn.root.setOnClickListener { showLogoutDialog() }
        }
    }

    private fun showLogoutDialog() {
        requireContext().alert(viewModel.logoutDialogModel,
            positiveAction = {
                dismiss()
                LoginActivity.startLoginScreen(requireContext())
            },
            negativeAction = { dismiss() }
        ).show()
    }
}