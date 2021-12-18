package com.example.wocombo.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val id: String,
    val symbol: String,
    val name: String,
    val nameId: String,
    val percentHour: String,
    val percentDay: String,
    val priceUsd: String,
    val volume: Double
): Parcelable