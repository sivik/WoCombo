package com.example.wocombo.core.domain.models

import org.joda.time.DateTime

data class Event(
    val date: DateTime,
    val id: String,
    val imageUrl: String,
    val subtitle: String,
    val title: String,
    val videoUrl: String
)