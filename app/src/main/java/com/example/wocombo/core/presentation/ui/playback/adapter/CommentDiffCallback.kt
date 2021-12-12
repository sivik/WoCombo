package com.example.wocombo.core.presentation.ui.playback.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.wocombo.core.presentation.model.Comment

object CommentDiffCallback: DiffUtil.ItemCallback<Comment>(){
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment) = oldItem.text == newItem.text
}