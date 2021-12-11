package com.example.wocombo.core.presentation.ui.transmissionlist.schedules.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.wocombo.core.domain.models.Schedule
import java.util.*

class ScheduleDiffCallback(
    private val newList: LinkedList<Schedule>,
    private val oldList: LinkedList<Schedule>
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
    }

    private fun getItemsFromCollections(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Pair<Schedule?, Schedule?> {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return Pair(oldItem, newItem)
    }

}