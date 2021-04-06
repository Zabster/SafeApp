package ru.zabster.safepassapp.screens.fragments.category_list.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.databinding.BottomSheetCategoryListBinding
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.base.screen.MainBottomSheetDialogFragment
import ru.zabster.safepassapp.screens.fragments.category_list.domin.CategoryListViewModelFactory
import ru.zabster.safepassapp.utils.extensions.hideKeyBoard

/**
 * Category list bottom sheet
 */
class CategoryListBottomSheet :
    MainBottomSheetDialogFragment<BottomSheetCategoryListBinding>(R.layout.bottom_sheet_category_list) {

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CategoryListViewModel by viewModels { viewModelFactory }
    private var categoryChooseFragmentCallback: CategoryListChooseCallback? = null

    private val itemChosenCallback = object : CategoryListChooseCallback {
        override fun chooseCategory(category: CategoryData) {
            chosenCategory(category)
        }
    }
    private val adapter = CategoryListAdapter(itemChosenCallback)

    private val newCategoryName: String
        get() = binding?.categoryNameEditText?.editText?.text.toString().trim()

    override fun createViewModelFactory(appComponents: AppComponents, mainComponent: MainComponents) {
        viewModelFactory = CategoryListViewModelFactory(mainComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initListeners()
        observeLiveDate()
    }

    private fun initListeners() {
        withFragmentBinding {
            addNewBtn.setOnClickListener {
                viewModel.addCategory(newCategoryName)
                binding?.categoryNameEditText?.editText?.text?.clear()
                requireActivity().hideKeyBoard()
            }
        }
    }

    private fun initRecycler() {
        withFragmentBinding {
            categoryList.adapter = adapter
        }
    }

    private fun observeLiveDate() {
        viewModel.categoryList.observe(viewLifecycleOwner, this::updateCategoryList)
    }

    private fun updateCategoryList(categories: List<CategoryData>) {
        adapter.submitList(categories)
    }

    private fun chosenCategory(category: CategoryData) {
        categoryChooseFragmentCallback?.chooseCategory(category)
        dismiss()
    }

    companion object {

        /**
         * New instance for CategoryListBottomSheet
         *
         * @return bottom sheet fragment
         */
        fun newInstance(categoryChooseCallback: CategoryListChooseCallback): CategoryListBottomSheet =
            CategoryListBottomSheet().apply {
                this.categoryChooseFragmentCallback = categoryChooseCallback
            }
    }
}