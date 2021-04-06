package ru.zabster.safepassapp.screens.fragments.new_data.presentation

import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import ru.zabster.safepassapp.R.string
import ru.zabster.safepassapp.db.AppDatabase
import ru.zabster.safepassapp.db.entities.CategoryEntity.TableInfo.DefaultValues
import ru.zabster.safepassapp.di.impls.rescontents.ResourceManager
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.fragments.data_list.data.CredentialData
import ru.zabster.safepassapp.screens.fragments.new_data.data.SaveDataState
import ru.zabster.safepassapp.screens.fragments.new_data.domain.CreateDataInteractor
import ru.zabster.safepassapp.utils.secure.SecurityUtils

/**
 * View model for screen new credential
 *
 * @param db database
 * @param resourceManager manager for resource access
 */
class CreateDataViewModel(
    db: AppDatabase,
    resourceManager: ResourceManager
) : ViewModel() {

    /**
     * Loading or not
     */
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    /**
     * Loading or not when decrypt password
     */
    private val _passLoadingState = MutableLiveData<Boolean>()
    val passLoadingState: LiveData<Boolean>
        get() = _passLoadingState

    /**
     * State when data in saving proccess
     */
    private val _dataCreationState = MutableLiveData<SaveDataState>()
    val dataCreationState: LiveData<SaveDataState>
        get() = _dataCreationState

    /**
     * Name chosen category
     */
    private val _chosenCategoryData = MutableLiveData<CategoryData>()
    val chosenCategoryText: LiveData<String>
        get() = Transformations.map(_chosenCategoryData) { chosenCategory ->
            chosenCategory?.name ?: defaultChosenCategory.name
        }

    /**
     * Data for editing
     */
    private val _editState = MutableLiveData<CredentialData?>()
    val editState: LiveData<CredentialData?>
        get() = _editState

    private val dataInteractor = CreateDataInteractor(db)

    private val defaultChosenCategory =
        CategoryData(
            id = DefaultValues.COLUMN_ID_DEF_NO_SET_VALUE,
            name = resourceManager.getString(string.new_credential_data_category_base_text)
        )

    /**
     * Initial state for screen by args
     */
    fun initByArgs(args: CredentialData?) {
        viewModelScope.launch {
            if (_chosenCategoryData.value == null) {
                args?.let { data ->
                    _passLoadingState.postValue(true)
                    _editState.postValue(args)

                    // choose category
                    dataInteractor.getCategoryById(data.categoryId)?.let { categoryData ->
                        chooseCategory(categoryData, false)
                    } ?: chooseCategory(defaultChosenCategory, false)

                    // set real password
                    SecurityUtils.secureKeyDataFromGson(data.hashPass)?.let { secureKeyData ->
                        data.pass = SecurityUtils.decryptSecret(
                            data = secureKeyData,
                            dispatcher = Dispatchers.IO
                        )
                    }
                    _editState.postValue(args)
                    _passLoadingState.postValue(false)

                } ?: chooseCategory(defaultChosenCategory, false)
            }
        }
    }

    /**
     * Save credential
     *
     * @param titleText require title
     * @param descText not require description
     * @param passText require pass text
     */
    fun saveNewCredential(titleText: String, descText: String, passText: String) {
        viewModelScope.launch {
            if (!checkRequiredFields(titleText, passText)) return@launch
            _loadingState.postValue(true)
            val isSave = _chosenCategoryData.value?.let { categoryData ->
                dataInteractor.saveCredential(titleText, descText, passText, categoryData.id)
            } ?: false
            _loadingState.postValue(false)
            _dataCreationState.postValue(SaveDataState.SaveProcess(isSave))
        }
    }

    /**
     * Select current category
     *
     * @param categoryData category info
     * @param withValidate check or not
     */
    fun chooseCategory(categoryData: CategoryData, withValidate: Boolean = true) {
        _chosenCategoryData.postValue(categoryData)
        if (withValidate) checkRequiredFields(categoryData)
    }

    private fun checkRequiredFields(titleText: String, passText: String): Boolean {
        val isTitleValid = titleText.isNotEmpty()
        val isPassValid = passText.isNotEmpty()
        val isCategoryValid = _chosenCategoryData.value?.let { data ->
            data.id != DefaultValues.COLUMN_ID_DEF_NO_SET_VALUE
        } ?: false

        _dataCreationState.postValue(
            SaveDataState.ValidateAll(isTitleValid, isPassValid, isCategoryValid)
        )

        return isTitleValid && isPassValid && isCategoryValid
    }

    private fun checkRequiredFields(categoryData: CategoryData) {
        val isCategoryValid = categoryData.id != DefaultValues.COLUMN_ID_DEF_NO_SET_VALUE
        _dataCreationState.postValue(
            SaveDataState.ValidateCategory(validCategory = isCategoryValid)
        )
    }
}