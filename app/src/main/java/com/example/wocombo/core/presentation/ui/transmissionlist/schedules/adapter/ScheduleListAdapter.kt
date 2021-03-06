package com.example.wocombo.core.presentation.ui.transmissionlist.schedules.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wocombo.R
import com.example.wocombo.common.extensions.getSpannableDateWithPeriodText
import com.example.wocombo.core.domain.models.Schedule
import com.example.wocombo.databinding.AdapterScheduleListItemBinding
import java.util.*

class ScheduleListAdapter: ListAdapter<Schedule, ScheduleListAdapter.ScheduleListViewHolder>(ScheduleDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListViewHolder {
        val binding =
            AdapterScheduleListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ScheduleListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleListViewHolder, position: Int) {
        val item = getItem(position)
        setTitleText(holder, item)
        setSubtitleText(holder, item)
        setTransmissionDate(holder, item)
        setImage(holder, item)
    }

    private fun setTitleText(holder: ScheduleListViewHolder, item: Schedule) {
        holder.binding.tvTitle.text = item.title
    }

    private fun setSubtitleText(holder: ScheduleListViewHolder, item: Schedule) {
        holder.binding.tvSubtitle.text = item.subtitle
    }

    private fun setImage(holder: ScheduleListViewHolder, item: Schedule) {
        Glide
            .with(holder.itemView.context)
            .load(item.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_downloading_24)
            .into(holder.binding.ivScheduleImage)

        holder.binding.tvSubtitle.text = item.subtitle
    }

    private fun setTransmissionDate(holder: ScheduleListViewHolder, item: Schedule) {
        holder.binding.tvDate.apply {
            val spannableText = getSpannableDateWithPeriodText(
                R.string.real_time_date,
                item.date
            )
            text = spannableText
        }
    }

    override fun submitList(list: List<Schedule>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class ScheduleListViewHolder(val binding: AdapterScheduleListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}