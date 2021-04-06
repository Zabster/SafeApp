package ru.zabster.safepassapp.screens.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.screens.login.data.LoginData
import ru.zabster.safepassapp.screens.login.domain.PasswordInteractor
import ru.zabster.safepassapp.utils.dialogs.DialogDataModel

/**
 * ViewModel for login screen
 *
 * @param resourceManager access to resource
 * @param sharedPreferenceHelper private local storage
 */
class LoginViewModel(
    private val resourceManager: ResourceManager,
    sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    /**
     * LiveData for login screen state
     */
    private val _loginState = MutableLiveData<LoginData>()
    val loginState: LiveData<LoginData>
        get() = _loginState

    /**
     * LiveData for enabled state for interactions ui component
     */
    private val _enableState = MutableLiveData(true)
    val enableState: LiveData<Boolean>
        get() = _enableState

    /**
     * Data for Empty storage warning dialog
     */
    val dialogDataEmptyStorage = DialogDataModel(
        title = resourceManager.getString(R.string.dialog_set_password_title_text),
        message = resourceManager.getString(R.string.dialog_set_password_message_text),
        positiveButtonText = resourceManager.getString(R.string.dialog_set_password_positive_action_text),
        negativeButtonText = resourceManager.getString(R.string.dialog_set_password_negative_action_text)
    )

    /**
     * Data for Empty storage warning dialog
     */
    val dialogDataIncorrectPassword = DialogDataModel(
        title = resourceManager.getString(R.string.dialog_warning_title),
        message = resourceManager.getString(R.string.dialog_incorrect_password_message_text),
        positiveButtonText = resourceManager.getString(R.string.dialog_incorrect_password_positive_action_text)
    )

    private val passwordInteractor = PasswordInteractor(sharedPreferenceHelper)

    /**
     * Try login
     *
     * @param password key for unlock database
     */
    fun onSignInClick(password: String) {
        actionWithUiLock { passwordInteractor.checkLoginState(password) }
    }

    /**
     * Create or change password
     *
     * @param password key for unlock database
     */
    fun setNewPassword(password: String) {
        actionWithUiLock { passwordInteractor.setPassword(password) }
    }

    private inline fun actionWithUiLock(crossinline action: suspend () -> LoginData) {
        viewModelScope.launch {
            _enableState.postValue(false)
            _loginState.postValue(action.invoke())
            _enableState.postValue(true)
        }
    }
}