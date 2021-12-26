package com.example.wocombo.core.domain.models

import android.os.Parcelable
import com.example.wocombo.core.domain.enums.Indicator
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val id: Int,
    val symbol: String,
    val name: String,
    val nameId: String,
    val percentHour: Double,
    val percentDay: Double,
    val priceUsd: Double,
    val volume: Double,
    var indicator: Indicator? = null
): Parcelable