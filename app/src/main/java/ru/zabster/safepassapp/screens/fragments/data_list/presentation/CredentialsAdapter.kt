package ru.zabster.safepassapp.screens.fragments.data_list.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.zabster.safepassapp.databinding.ItemAdapterCredentialsViewBinding
import ru.zabster.safepassapp.screens.base.domain.ItemClickListener

import ru.zabster.safepassapp.screens.base.other.BaseViewHolder
import ru.zabster.safepassapp.screens.fragments.data_list.data.CredentialData
import ru.zabster.safepassapp.screens.fragments.data_list.presentation.CredentialsAdapter.CredentialViewHolder

/**
 * List adapter for credentials
 *
 * @param clickListener click callback with [CredentialData] item
 */
class CredentialsAdapter(private val clickListener: ItemClickListener<CredentialData>) :
    ListAdapter<CredentialData, CredentialViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CredentialViewHolder =
        CredentialViewHolder.create(parent, clickListener)

    override fun onBindViewHolder(holder: CredentialViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * Holder view for credential
     *
     * @param binding view binding
     */
    class CredentialViewHolder private constructor(
        private val binding: ItemAdapterCredentialsViewBinding,
        private val clickListener: ItemClickListener<CredentialData>
    ) : BaseViewHolder<ItemAdapterCredentialsViewBinding, CredentialData>(binding) {

        override fun bind(item: CredentialData) {
            with(binding) {
                itemData = item
                itemView.setOnClickListener { clickListener.onItemClick(item) }
                executePendingBindings()
            }
        }

        companion object {

            /**
             * Create VH for adapter item
             *
             * @param parent parent view group
             * @param clickListener click callback
             *
             * @return binding view
             */
            fun create(parent: ViewGroup, clickListener: ItemClickListener<CredentialData>): CredentialViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemAdapterCredentialsViewBinding.inflate(inflater, parent, false)
                return CredentialViewHolder(binding, clickListener)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CredentialData>() {

        override fun areItemsTheSame(oldItem: CredentialData, newItem: CredentialData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CredentialData, newItem: CredentialData): Boolean =
            oldItem == newItem
    }
}