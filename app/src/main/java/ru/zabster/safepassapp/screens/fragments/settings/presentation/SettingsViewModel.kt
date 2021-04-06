package ru.zabster.safepassapp.screens.fragments.settings.presentation

import androidx.lifecycle.ViewModel
import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager

import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.screens.fragments.settings.data.DarkModel
import ru.zabster.safepassapp.utils.dialogs.DialogDataModel

/**
 * Settings screen view model
 */
class SettingsViewModel(
    resourceManager: ResourceManager,
    sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    val darkModeState: DarkModel = DarkModel(sharedPreferenceHelper)

    val logoutDialogModel = DialogDataModel(
        title = resourceManager.getString(R.string.dialog_warning_title),
        message = resourceManager.getString(R.string.dialog_logout_message),
        positiveButtonText = resourceManager.getString(R.string.dialog_button_positive),
        negativeButtonText = resourceManager.getString(R.string.dialog_button_negative)
    )

}