package com.example.wocombo.core.presentation.ui.transmissionlist.currencies.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.wocombo.core.domain.models.Currency


object CurrencyDiffCallback : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.id == newItem.id &&
                oldItem.nameId ==  newItem.nameId
    }
}