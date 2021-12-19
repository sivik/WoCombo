package com.example.wocombo.core.data.model

data class CurrencyEntity(
    val id: Int,
    val symbol: String,
    val name: String,
    val nameId: String,
    val percentHour: String,
    val percentDay: String,
    val priceUsd: Double,
    val volume: Double
)