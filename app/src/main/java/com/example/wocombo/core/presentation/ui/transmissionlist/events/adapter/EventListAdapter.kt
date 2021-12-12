package com.example.wocombo.core.presentation.ui.transmissionlist.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wocombo.R
import com.example.wocombo.common.extensions.getSpannableDateWithPeriodText
import com.example.wocombo.core.domain.models.Event
import com.example.wocombo.core.presentation.enums.SortType
import com.example.wocombo.databinding.AdapterEventListItemBinding
import java.util.*

class EventListAdapter(
    private val events: LinkedList<Event>
) : RecyclerView.Adapter<EventListAdapter.EventListViewHolder>() {

    lateinit var onEventSelected: (Event) -> Unit
    lateinit var onReminderAdded: (Event) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val binding =
            AdapterEventListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventListViewHolder(binding)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        val item = events[position]
        setTitleText(holder, item)
        setSubtitleText(holder, item)
        setTransmissionDate(holder, item)
        setImage(holder, item)
        initListeners(holder, item)
    }

    private fun initListeners(holder: EventListViewHolder, item: Event) {
        holder.binding.cvEventCard.setOnClickListener {
            onEventSelected.invoke(item)
        }

        holder.binding.ibAddReminder.setOnClickListener {
            onReminderAdded.invoke(item)
        }
    }

    private fun setTitleText(holder: EventListViewHolder, item: Event) {
        holder.binding.tvTitle.text = item.title
    }

    private fun setSubtitleText(holder: EventListViewHolder, item: Event) {
        holder.binding.tvSubtitle.text = item.subtitle
    }

    private fun setImage(holder: EventListViewHolder, item: Event){
        Glide
            .with(holder.itemView.context)
            .load(item.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_downloading_24)
            .into(holder.binding.ivEventImage)

        holder.binding.tvSubtitle.text = item.subtitle
    }

    private fun setTransmissionDate(holder: EventListViewHolder, item: Event) {
        holder.binding.tvDate.apply {
            val spannableText = getSpannableDateWithPeriodText(
                R.string.real_time_date,
                item.date
            )
            text = spannableText
        }
    }

    fun update(newEvents: List<Event>, sortType: SortType) {
        val diffCallback = EventDiffCallback(LinkedList(events), LinkedList(newEvents))
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        when(sortType){
            SortType.ASCENDING -> newEvents.sortedBy { it.date }
            SortType.DESCENDING -> newEvents.sortedByDescending{ it.date }
        }
        events.clear()
        events.addAll(newEvents)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getEventList(): LinkedList<Event>{
        return events
    }

    class EventListViewHolder(val binding: AdapterEventListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}