package com.example.wocombo.core.presentation.ui.transmissionlist.schedules.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.wocombo.core.domain.models.Schedule
import java.util.*


object ScheduleDiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.id == newItem.id &&
                oldItem.date.millis ==  oldItem.date.millis
    }
}