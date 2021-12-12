package com.example.wocombo.core.presentation.ui.reminders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wocombo.common.date.DateTimePatterns
import com.example.wocombo.core.domain.models.Reminder
import com.example.wocombo.databinding.AdapterReminderListItemBinding

class RemindersAdapter :
    PagingDataAdapter<Reminder, RemindersAdapter.ReminderListViewHolder>(REMINDER_DIFF_CALLBACK) {

    lateinit var onReminderDeleted: (Reminder) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderListViewHolder {
        val binding =
            AdapterReminderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReminderListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ReminderListViewHolder, position: Int) {
        getItem(position)?.let { reminder ->
            setReminderInfo(reminder, holder)
            setDeleteButton(holder, reminder)
        }
    }

    private fun setReminderInfo(reminder: Reminder, holder: ReminderListViewHolder){
        holder.binding.tvDate.text = reminder.date.toString(DateTimePatterns.DOT_DATE_TIME)
        holder.binding.tvTitle.text = reminder.title
    }

    private fun setDeleteButton(holder: ReminderListViewHolder, item: Reminder) {
        holder.binding.ibDelete.setOnClickListener {
            onReminderDeleted.invoke(item)
        }
    }

    class ReminderListViewHolder(val binding: AdapterReminderListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val REMINDER_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Reminder>() {
            override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
                oldItem.id == newItem.id && oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
                oldItem == newItem
        }
    }
}