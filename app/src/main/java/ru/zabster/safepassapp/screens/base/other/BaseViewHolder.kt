package ru.zabster.safepassapp.screens.base.other

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Base class for create [ViewHolder] for [ListAdapter] use [ViewDataBinding]
 *
 * @param binding view binding
 */
abstract class BaseViewHolder<V: ViewDataBinding, I>(binding: V): ViewHolder(binding.root) {

    /**
     * Set item data
     *
     * @param item data model
     */
    abstract fun bind(item: I)
}