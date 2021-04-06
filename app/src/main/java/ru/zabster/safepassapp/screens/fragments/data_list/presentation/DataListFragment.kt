package ru.zabster.safepassapp.screens.fragments.data_list.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import ru.zabster.safepassapp.R
import ru.zabster.safepassapp.databinding.FragmentDataListBinding
import ru.zabster.safepassapp.di.AppComponents
import ru.zabster.safepassapp.di.MainComponents
import ru.zabster.safepassapp.screens.base.screen.MainFragmentScreen
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.base.domain.ItemClickListener
import ru.zabster.safepassapp.screens.fragments.data_list.data.CredentialData
import ru.zabster.safepassapp.screens.fragments.data_list.domain.DataListViewModelFactory
import ru.zabster.safepassapp.screens.main.presentation.CategoryActionSharedViewModel
import ru.zabster.safepassapp.utils.dialogs.alert
import ru.zabster.safepassapp.utils.dialogs.bottomSheet
import ru.zabster.safepassapp.utils.dialogs.loadingDialog

/**
 * Data list screen
 */
class DataListFragment : MainFragmentScreen<FragmentDataListBinding>(R.layout.fragment_data_list) {

    private lateinit var itemAdapter: CredentialsAdapter
    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: DataListViewModel by viewModels { viewModelFactory }
    private val categorySharedViewModel: CategoryActionSharedViewModel by activityViewModels()

    private lateinit var decryptLoadingDialog: AlertDialog

    override fun createViewModelFactory(appComponents: AppComponents, mainComponent: MainComponents) {
        viewModelFactory = DataListViewModelFactory(mainComponent, appComponents)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkData()
        itemAdapter.registerAdapterDataObserver(adapterObserver)
    }

    override fun onPause() {
        super.onPause()
        itemAdapter.unregisterAdapterDataObserver(adapterObserver)
    }

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            if (itemCount == LAST_ITEM) {
                viewModel.checkData()
            }
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            binding?.credentialList?.layoutManager?.scrollToPosition(START_POSITION)
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int =
            makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.START or ItemTouchHelper.END)

        override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean = false

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            viewModel.deleteCredential(viewHolder.adapterPosition)
        }
    })

    private val onItemClickListener = object : ItemClickListener<CredentialData> {
        override fun onItemClick(item: CredentialData) {
            showChooseBottomSheet(item)
        }
    }

    private fun showChooseBottomSheet(item: CredentialData) {
        requireContext().bottomSheet(
            item.name,
            listOf(
                Pair(requireContext().getString(R.string.list_data_edit_credential), item),
                Pair(requireContext().getString(R.string.list_data_show_password), item),
            )
        ) { index, chosenData ->
            when (index) {
                INDEX_EDIT -> {
                    val action = DataListFragmentDirections
                        .actionDataListFragmentToCreateDataFragment(chosenData)
                    navigate(action)
                }
                INDEX_SHOW_PASS -> {
                    showPassword(chosenData)
                }
            }
            dismiss()
        }.show()
    }

    private fun showPassword(chosenData: CredentialData) {
        showDecryptLoading()
        viewModel.decryptPassword(chosenData.hashPass)
    }

    private fun initView() {
        withFragmentBinding {
            decryptLoadingDialog = requireContext().loadingDialog(getString(R.string.dialog_loading_decrypt_message))
            vm = viewModel
            itemAdapter = CredentialsAdapter(onItemClickListener)
            credentialList.adapter = itemAdapter
            credentialList.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            itemTouchHelper.attachToRecyclerView(credentialList)
        }
    }

    private fun observeLiveData() {
        viewModel.categoryList.observe(viewLifecycleOwner, this::updateCategoryList)
        viewModel.credentialsList.observe(viewLifecycleOwner, this::updateCredentialsList)
        categorySharedViewModel.checkedCategoryLiveData.observe(viewLifecycleOwner, this::handleCheckState)
        categorySharedViewModel.deleteCategoryLiveData.observe(viewLifecycleOwner, viewModel::deleteCategory)

        viewModel.decryptState.asLiveData().observe(viewLifecycleOwner, this::handleDecrypt)
    }

    private fun handleDecrypt(state: Pair<Boolean, String>) {
        dismissDecryptLoading()
        val (isSuccess, password) = state
        if (isSuccess) {
            showPasswordDialog(password)
        } else {
            showErrorDialog()
        }
    }

    private fun showPasswordDialog(password: String) {
        requireContext()
            .alert(viewModel.successPassDialogModel(password), positiveAction = { dismiss() })
            .show()
    }

    private fun showErrorDialog() {
        requireContext()
            .alert(viewModel.errorDecryptData, positiveAction = { dismiss() })
            .show()
    }

    private fun handleCheckState(pair: Pair<CategoryData, Boolean>) {
        val (categoryData, isSelected) = pair
        viewModel.setCheckChip(categoryData, isSelected)
    }

    private fun updateCategoryList(data: List<CategoryData>) {
        categorySharedViewModel.setCategoryList(data)
    }

    private fun updateCredentialsList(data: List<CredentialData>) {
        itemAdapter.submitList(data)
    }

    private fun showDecryptLoading() {
        if (!decryptLoadingDialog.isShowing)
            decryptLoadingDialog.show()
    }

    private fun dismissDecryptLoading() {
        if (decryptLoadingDialog.isShowing)
            decryptLoadingDialog.dismiss()
    }

    companion object {

        private const val LAST_ITEM = 1
        private const val START_POSITION = 0

        private const val INDEX_EDIT = 0
        private const val INDEX_SHOW_PASS = 1
    }
}