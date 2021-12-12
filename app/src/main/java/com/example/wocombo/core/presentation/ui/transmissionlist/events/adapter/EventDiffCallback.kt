package com.example.wocombo.core.presentation.ui.transmissionlist.events.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.wocombo.core.domain.models.Event
import java.util.LinkedList

class EventDiffCallback(
    private val newList: LinkedList<Event>,
    private val oldList: LinkedList<Event>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItemsFromCollections(oldItemPosition, newItemPosition)
        return oldItem == newItem
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItemsFromCollections(oldItemPosition, newItemPosition)

        return oldItem?.title == newItem?.title &&
                oldItem?.id == newItem?.id &&
                oldItem?.subtitle == newItem?.subtitle
                && oldItem?.date == newItem?.date

    }

    private fun getItemsFromCollections(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Pair<Event?, Event?> {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return Pair(oldItem, newItem)
    }

}