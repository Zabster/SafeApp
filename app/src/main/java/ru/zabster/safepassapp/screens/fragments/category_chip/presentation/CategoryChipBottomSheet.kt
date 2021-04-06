package ru.zabster.safepassapp.screens.fragments.category_chip.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.databinding.BottomSheetCategoryChipsBinding
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.base.screen.BaseBottomSheetDialogFragment
import ru.zabster.safepassapp.screens.main.presentation.CategoryActionSharedViewModel

/**
 * Category chips sorting bottom sheet
 */
class CategoryChipBottomSheet :
    BaseBottomSheetDialogFragment<BottomSheetCategoryChipsBinding>(R.layout.bottom_sheet_category_chips) {

    private val sharedViewModel: CategoryActionSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData() {
        sharedViewModel.categoryList.observe(viewLifecycleOwner, this::updateCategoryList)
    }

    private fun updateCategoryList(list: List<CategoryData>) {
        withFragmentBinding {
            chipGroup.removeAllViews()
            list.map(this@CategoryChipBottomSheet::createChip).forEach { chip ->
                chipGroup.addView(chip)
            }
        }
    }

    private fun createChip(data: CategoryData): Chip =
        Chip(requireContext()).apply {
            id = ViewCompat.generateViewId()

            text = data.name
            isCloseIconVisible = data.canDelete

            isCheckedIconVisible = false
            isCheckable = true
            isChecked = data.isSelect

            setTextAppearanceResource(R.style.TextStyle_BaseText)

            setOnClickListener { if (!isChecked) isChecked = true }
            setOnCloseIconClickListener { sharedViewModel.deleteCategory(data) }
            setOnCheckedChangeListener { _, b -> sharedViewModel.selectCategory(data, b) }
        }

    companion object {

        /**
         * New instance for CategoryChipBottomSheet
         *
         * @return bottom sheet fragment
         */
        fun newInstance(): CategoryChipBottomSheet = CategoryChipBottomSheet()
    }
}