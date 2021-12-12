package com.example.wocombo.core.presentation.model

import android.graphics.drawable.Drawable

data class Comment(
    val avatar: Drawable,
    val username: String,
    val text: String
)