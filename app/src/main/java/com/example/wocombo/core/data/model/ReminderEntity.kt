package com.example.wocombo.core.data.model

import org.joda.time.DateTime

data class ReminderEntity(
    val id: Int,
    val eventId: Int,
    val title: String,
    val date: DateTime
)