package ru.zabster.safepassapp.screens.fragments.category_list.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.zabster.safepassapp.databinding.ItemAdapterCategoryViewBinding
import ru.zabster.safepassapp.screens.base.data.CategoryData
import ru.zabster.safepassapp.screens.base.other.BaseViewHolder
import ru.zabster.safepassapp.screens.fragments.category_list.presentation.CategoryListAdapter.CategoryListViewHolder

/**
 * Adapter for category items
 *
 * @param categoryChoose callback chosen category
 */
class CategoryListAdapter(private val categoryChoose: CategoryListChooseCallback) :
    ListAdapter<CategoryData, CategoryListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder =
        CategoryListViewHolder.create(parent, categoryChoose)

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Category item view
     */
    class CategoryListViewHolder private constructor(
        private val binding: ItemAdapterCategoryViewBinding,
        private val categoryChoose: CategoryListChooseCallback
    ) : BaseViewHolder<ItemAdapterCategoryViewBinding, CategoryData>(binding) {

        override fun bind(item: CategoryData) {
            with(binding) {
                itemData = item
                itemView.setOnClickListener { categoryChoose.chooseCategory(item) }
                executePendingBindings()
            }
        }

        companion object {

            /**
             * Create category view holder
             *
             * @param parent parent view group
             */
            fun create(parent: ViewGroup, categoryChoose: CategoryListChooseCallback): CategoryListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemAdapterCategoryViewBinding.inflate(inflater, parent, false)
                return CategoryListViewHolder(binding, categoryChoose)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CategoryData>() {

        override fun areItemsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean =
            oldItem == newItem
    }
}