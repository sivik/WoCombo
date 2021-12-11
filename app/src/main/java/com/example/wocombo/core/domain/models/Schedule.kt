package com.example.wocombo.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
data class Schedule(
    val date: DateTime,
    val id: String,
    val imageUrl: String,
    val subtitle: String,
    val title: String
): Parcelable