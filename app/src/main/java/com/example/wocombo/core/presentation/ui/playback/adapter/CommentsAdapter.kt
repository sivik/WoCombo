package com.example.wocombo.core.presentation.ui.playback.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wocombo.core.presentation.model.Comment
import com.example.wocombo.databinding.AdapterCommentListItemBinding

class CommentsAdapter : ListAdapter<Comment, CommentsAdapter.ViewHolder>(CommentDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterCommentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            ivAvatar.setImageDrawable(item.avatar)
            tvText.text = item.text
            tvUsername.text = item.username
        }
    }

    class ViewHolder(val binding: AdapterCommentListItemBinding) : RecyclerView.ViewHolder(binding.root)
}

