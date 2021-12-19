package com.example.wocombo.core.domain.models

import org.joda.time.DateTime

data class HistoryCurrency(
    val id: Int?,
    val currencyId: Int,
    val nameId: String,
    val priceUsd: Double,
    val date: DateTime
)