package ru.zabster.safepassapp.screens.login.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.databinding.ActivityLoginBinding
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.screens.base.screen.BaseActivityBindingScreen
import ru.zabster.safepassapp.screens.login.data.LoginData
import ru.zabster.safepassapp.screens.login.data.LoginErrorType
import ru.zabster.safepassapp.screens.login.data.LoginState
import ru.zabster.safepassapp.screens.login.domain.LoginViewModelFactory
import ru.zabster.safepassapp.screens.main.presentation.MainActivity
import ru.zabster.safepassapp.utils.dialogs.alert

/**
 * Login screen
 *
 * We need check or create password
 */
//TODO сделать анимацию загрузки (вылет вниз)
class LoginActivity : BaseActivityBindingScreen<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var viewModelFactory: LoginViewModelFactory
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }
    private val passwordTextWatcher: TextWatcher = getPasswordTextWatcher()

    private lateinit var emptyStorageDialog: AlertDialog
    private lateinit var incorrectPasswordDialog: AlertDialog

    private val passwordText: String
        get() = binding.passwordInput.text.toString().trim()

    override fun initInject(components: AppComponents) {
        viewModelFactory = LoginViewModelFactory(components)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initDialogs()
        listenUIAction()
        observeLiveData()
    }

    override fun onResume() {
        super.onResume()
        binding.passwordInput.addTextChangedListener(passwordTextWatcher)
    }

    override fun onPause() {
        binding.passwordInput.removeTextChangedListener(passwordTextWatcher)
        super.onPause()
    }

    private fun initViewModel() {
        with(binding) {
            lifecycleOwner = this@LoginActivity
            vm = viewModel
        }
    }

    private fun initDialogs() {
        emptyStorageDialog = alert(
            viewModel.dialogDataEmptyStorage,
            positiveAction = {
                viewModel.setNewPassword(passwordText)
                dismiss()
            },
            negativeAction = { dismiss() }
        )

        incorrectPasswordDialog = alert(
            viewModel.dialogDataIncorrectPassword,
            positiveAction = { dismiss() }
        )
    }

    private fun listenUIAction() {
        with(binding) {
            loginButton.setOnClickListener {
                viewModel.onSignInClick(passwordText)
            }
        }
    }

    private fun observeLiveData() {
        viewModel.loginState.observe(this, this::updateLoginState)
    }

    private fun getPasswordTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (!char.isNullOrEmpty()) clearEmptyFieldError()
            else setEmptyFieldError()
        }
    }

    private fun updateLoginState(loginData: LoginData) {
        when (loginData.state) {
            LoginState.Success -> handleSuccess()
            LoginState.Error -> handleError(loginData.error)
        }
    }

    private fun handleSuccess() {
        MainActivity.startNewMainScreen(this)
    }

    private fun handleError(errorType: LoginErrorType) {
        when (errorType) {
            LoginErrorType.SYSTEM_ERROR -> {
                // todo продумать решение для ошибок, которые мешают зайти в приложение (в идеале их быть не должно)
                // todo продумать фичу выгрузки паролей в зашифровоном виде
            }
            LoginErrorType.EMPTY_PASSWORD_IN_STORAGE -> showEmptyStorageDialog()
            LoginErrorType.INCORRECT_PASSWORD -> showIncorrectPasswordDialog()
            LoginErrorType.EMPTY_FIELD -> setEmptyFieldError()
            LoginErrorType.NONE -> {
                // do nothing
            }
        }
    }

    private fun showEmptyStorageDialog() {
        if (!emptyStorageDialog.isShowing) emptyStorageDialog.show()
    }

    private fun showIncorrectPasswordDialog() {
        if (!incorrectPasswordDialog.isShowing) incorrectPasswordDialog.show()
    }

    private fun setEmptyFieldError() {
        binding.passwordInputField.error = getString(R.string.login_input_empty_error_text)
    }

    private fun clearEmptyFieldError() {
        binding.passwordInputField.error = ""
    }

    companion object {

        /**
         * Start new login screen with clear state
         */
        fun startLoginScreen(context: Context) {
            context.startActivity(
                Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
        }
    }
}