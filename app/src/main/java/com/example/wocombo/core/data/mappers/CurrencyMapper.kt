package com.example.wocombo.core.data.mappers

import com.example.wocombo.core.data.adapters.remote.rest.response.CurrencyRemoteModel
import com.example.wocombo.core.data.model.CurrencyEntity
import com.example.wocombo.core.domain.models.Currency

fun CurrencyRemoteModel.mapToEntity() = CurrencyEntity(
    id = id,
    symbol = symbol,
    name = name,
    nameId = nameid,
    percentHour = percent_change_1h,
    percentDay = percent_change_24h,
    priceUsd = price_usd,
    volume = volume24
)

fun CurrencyEntity.mapToDomain() = Currency(
    id = id,
    symbol = symbol,
    name = name,
    nameId = nameId,
    percentHour = percentHour,
    percentDay = percentDay,
    priceUsd = priceUsd,
    volume = volume
)