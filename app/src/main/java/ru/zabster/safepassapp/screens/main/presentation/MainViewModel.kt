package ru.zabster.safepassapp.screens.main.presentation

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.db.AppDatabase
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager
import ru.zabster.safepassapp.di.impls.shared.SharedPreferenceHelper
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.fragments.data_list.domain.DataInteractor
import ru.zabster.safepassapp.screens.main.data.MainScreenState
import ru.zabster.safepassapp.screens.main.data.MainScreenState.*
import ru.zabster.safepassapp.screens.main.domain.MainModuleCreator
import ru.zabster.safepassapp.utils.dialogs.DialogDataModel

/**
 * ViewModel for main screen
 *
 * @param resourceManager access to resource
 * @param sharedPreferenceHelper private local storage
 */
class MainViewModel(
    private val resourceManager: ResourceManager,
    sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    /**
     * LiveData for screen state
     */
    private val _screenState = MutableLiveData<MainScreenState>()
    val screenState: LiveData<MainScreenState>
        get() = _screenState

    /**
     * Current nav state
     */
    var navState: Bundle? = null

    /**
     * Current bar visible state.
     * Need for fix visible appbar, when configuration change
     */
    var isShow: Boolean = false

    /**
     * Data for error initialize dialog
     */
    val dialogDataInitializeError = DialogDataModel(
        title = resourceManager.getString(R.string.dialog_error_title),
        message = resourceManager.getString(R.string.dialog_fail_initialize_message_text),
        positiveButtonText = resourceManager.getString(R.string.dialog_fail_initialize_positive_action_text),
        isCancelable = false
    )

    var mainComponents: MainComponents? = null
        private set

    private val moduleCreator = MainModuleCreator(sharedPreferenceHelper, Dispatchers.IO)

    override fun onCleared() {
        super.onCleared()
        navState = null
        mainComponents = null
    }

    /**
     * Try initialize mainComponent with secure database
     */
    fun initMainComponent(context: Context) {
        viewModelScope.launch {
            if (mainComponents != null) {
                _screenState.postValue(INITIALIZE_SUCCESS)
            } else {
                moduleCreator.initializeModule(context)?.let { components ->
                    mainComponents = components
                    _screenState.postValue(INITIALIZE_SUCCESS)
                } ?: run { _screenState.postValue(INITIALIZE_ERROR) }
            }
        }
    }
}