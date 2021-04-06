package ru.zabster.safepassapp.screens.fragments.new_data.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.databinding.FragmentNewDataBinding
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.base.screen.MainFragmentScreen
import ru.zabster.safepassapp.screens.fragments.category_list.presentation.CategoryListBottomSheet
import ru.zabster.safepassapp.screens.fragments.category_list.presentation.CategoryListChooseCallback
import ru.zabster.safepassapp.screens.fragments.data_list.data.CredentialData
import ru.zabster.safepassapp.screens.fragments.new_data.data.SaveDataState
import ru.zabster.safepassapp.screens.fragments.new_data.data.SaveDataState.SaveProcess
import ru.zabster.safepassapp.screens.fragments.new_data.data.SaveDataState.ValidateAll
import ru.zabster.safepassapp.screens.fragments.new_data.data.SaveDataState.ValidateCategory
import ru.zabster.safepassapp.screens.fragments.new_data.domain.CreateDataViewModelFactory

/**
 * Screen add new credential
 */
class CreateDataFragment : MainFragmentScreen<FragmentNewDataBinding>(R.layout.fragment_new_data),
    CategoryListChooseCallback {

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CreateDataViewModel by viewModels { viewModelFactory }

    private val titleText: String
        get() = binding?.credTitle?.editText?.text.toString().trim()

    private val descText: String
        get() = binding?.credDesc?.editText?.text.toString().trim()

    private val passText: String
        get() = binding?.credPass?.editText?.text.toString().trim()

    private val args: CreateDataFragmentArgs by navArgs()

    override fun createViewModelFactory(appComponents: AppComponents, mainComponent: MainComponents) {
        viewModelFactory = CreateDataViewModelFactory(appComponents, mainComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initListeners()
        observeStates()
        viewModel.initByArgs(args.credential)
    }

    override fun chooseCategory(category: CategoryData) {
        viewModel.chooseCategory(category)
    }

    private fun initViewModel() {
        withFragmentBinding {
            vm = viewModel
        }
    }

    private fun initListeners() {
        withFragmentBinding {
            saveButton.setOnClickListener {
                viewModel.saveNewCredential(titleText, descText, passText)
            }
            categorySpinner.editText?.setOnClickListener { showCategoryList() }
            materialToolbar.setNavigationOnClickListener { pop() }
        }
    }

    private fun observeStates() {
        viewModel.dataCreationState.observe(viewLifecycleOwner, this::handleState)
        viewModel.editState.observe(viewLifecycleOwner, this::handleIsEditState)
    }

    private fun handleIsEditState(credentialData: CredentialData?) {
        withFragmentBinding {
            credentialData?.let { data ->
                credTitle.editText?.setText(data.name)
                credDesc.editText?.setText(data.description)
                credPass.editText?.setText(data.pass)
                saveButton.text = getString(R.string.new_credential_data_update_btn_text)
            }
        }
    }

    private fun handleState(state: SaveDataState) {
        when (state) {
            is SaveProcess -> handleCreation(state.isComplete)
            is ValidateAll -> handleValidateAll(state)
            is ValidateCategory -> handleValidateCategory(state.validCategory)
        }
    }

    private fun handleCreation(saved: Boolean) {
        if (saved) pop()
        else showErrorSaveDialog()
    }

    private fun handleValidateAll(validate: ValidateAll) {
        withFragmentBinding {
            credTitle.error =
                if (validate.validTitle) null else getString(R.string.error_text_invalid_require_fields)
            credPass.error =
                if (validate.validPass) null else getString(R.string.error_text_invalid_require_fields)
            handleValidateCategory(validate.validCategory)
        }
    }

    private fun handleValidateCategory(validCategory: Boolean) {
        withFragmentBinding {
            categorySpinner.error = if (validCategory) null else getString(R.string.error_text_invalid_require_fields)
        }
    }

    private fun showErrorSaveDialog() {
        // todo придумать диалог с текстом и дальнейшие действия
    }

    private fun showCategoryList() {
        CategoryListBottomSheet.newInstance(this)
            .show(parentFragmentManager, CATEGORY_FRAGMENT_TAG)
    }

    companion object {

        private const val CATEGORY_FRAGMENT_TAG = "CategoryList"
    }
}