package com.example.wocombo.core.domain.models

import org.joda.time.DateTime

data class Reminder(
    val id: Int,
    val title: String,
    val date: DateTime
)