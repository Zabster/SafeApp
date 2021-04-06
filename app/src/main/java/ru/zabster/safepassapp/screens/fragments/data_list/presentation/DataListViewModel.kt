package ru.zabster.safepassapp.screens.fragments.data_list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.zabster.safepassapp.R

import ru.zabster.safepassapp.db.AppDatabase
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager
import ru.zabster.safepassapp.screens.base.other.DatabaseValidator
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.fragments.data_list.data.CredentialData
import ru.zabster.safepassapp.screens.fragments.data_list.data.DataListScreenState
import ru.zabster.safepassapp.screens.fragments.data_list.domain.CategoryTableValidator
import ru.zabster.safepassapp.screens.fragments.data_list.domain.DataInteractor
import ru.zabster.safepassapp.utils.dialogs.DialogDataModel
import ru.zabster.safepassapp.utils.secure.SecurityUtils

/**
 * Data list viewModel
 *
 * @param db secure Database
 */
class DataListViewModel(
    private val db: AppDatabase,
    private val resourceManager: ResourceManager
) : ViewModel() {

    /**
     * List of category
     */
    val categoryList: LiveData<List<CategoryData>>
        get() = dataInteractor.categoryListLiveData

    /**
     * List of credential
     */
    private val _credentialsList = MutableLiveData<List<CredentialData>>()
    val credentialsList: LiveData<List<CredentialData>>
        get() = _credentialsList

    /**
     * Screen state live data as [DataListScreenState]
     */
    private val _screenState = MutableLiveData<DataListScreenState>()
    val screenState: LiveData<DataListScreenState>
        get() = _screenState

    /**
     * State decrypt -> isSuccess and value
     */
    private val _decryptState = Channel<Pair<Boolean, String>>(Channel.BUFFERED)
    val decryptState = _decryptState.receiveAsFlow()

    val errorDecryptData = DialogDataModel(
        title = resourceManager.getString(R.string.dialog_warning_title),
        message = resourceManager.getString(R.string.dialog_password_error_message),
        positiveButtonText = resourceManager.getString(R.string.dialog_button_positive)
    )

    private val tableValidator: DatabaseValidator = CategoryTableValidator(db.categoryDao())
    private val dataInteractor = DataInteractor(db)

    /**
     * Validate table if it's empty
     */
    init {
        viewModelScope.launch {
            tableValidator.validateAndRepair()
        }
    }

    /**
     * Checking data state, empty or not
     */
    fun checkData() {
        viewModelScope.launch {
            val state = dataInteractor.haveData()
            if (state == DataListScreenState.NotEmptyList)
                updateListByCategory(dataInteractor.getSelectedCategoryId())
            else _screenState.postValue(state)
        }
    }

    /**
     * Update chip state
     *
     * @param category info about category
     * @param isSelect select or not
     */
    fun setCheckChip(category: CategoryData, isSelect: Boolean) {
        viewModelScope.launch {
            dataInteractor.updateCategory(category.copy(isSelect = isSelect))
            if (isSelect) updateListByCategory(category.id)
        }
    }

    /**
     * Delete category
     *
     * @param category info about category
     */
    fun deleteCategory(category: CategoryData) {
        viewModelScope.launch {
            dataInteractor.deleteCategory(category)
            updateListByCategory()
        }
    }

    /**
     * Delete credential
     *
     * @param position position on list
     */
    fun deleteCredential(position: Int) {
        viewModelScope.launch {
            _credentialsList.value?.let { credentialList ->
                dataInteractor.deleteCredential(credentialList[position])
                _credentialsList.postValue(
                    ArrayList(credentialList.filterIndexed { index, _ -> index != position })
                )
            }
        }
    }

    /**
     * Decrypt password process
     *
     * @param hashPass pass cred
     */
    fun decryptPassword(hashPass: String) {
        viewModelScope.launch {
            SecurityUtils.secureKeyDataFromGson(hashPass)?.let { secureKeyData ->
                val password = SecurityUtils.decryptSecret(secureKeyData, Dispatchers.IO)
                _decryptState.send(Pair(true, password))
            } ?: _decryptState.send(Pair(false, ""))
        }
    }

    fun successPassDialogModel(password: String) = DialogDataModel(
        title = resourceManager.getString(R.string.dialog_password_success_title),
        message = password,
        isSelectable = true,
        positiveButtonText = resourceManager.getString(R.string.dialog_button_positive)
    )

    private suspend fun updateListByCategory(categoryId: Long? = null) {
        _credentialsList.postValue(dataInteractor.getCredentialListByCategory(categoryId))
        _screenState.postValue(DataListScreenState.NotEmptyList)
    }
}
