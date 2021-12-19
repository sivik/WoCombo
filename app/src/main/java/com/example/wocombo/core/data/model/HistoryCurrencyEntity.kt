package com.example.wocombo.core.data.model

import org.joda.time.DateTime

data class HistoryCurrencyEntity(
    val id: Int?,
    val currencyId: Int,
    val nameId: String,
    val priceUsd: Double,
    val date: DateTime
)