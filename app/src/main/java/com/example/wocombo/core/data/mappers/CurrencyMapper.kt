package com.example.wocombo.core.data.mappers

import com.example.wocombo.core.data.adapters.remote.rest.response.CurrencyRemoteModel
import com.example.wocombo.core.data.model.CurrencyEntity
import com.example.wocombo.core.data.model.HistoryCurrencyEntity
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.models.HistoryCurrency
import com.example.wocombo.database.data.model.HistoryCurrencyDbModel
import org.joda.time.DateTime

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
    percentHour = percentHour.toDouble(),
    percentDay = percentDay.toDouble(),
    priceUsd = priceUsd,
    volume = volume
)

fun HistoryCurrencyEntity.mapToDbModel() = HistoryCurrencyDbModel(
    currencyId = currencyId,
    nameId = nameId,
    priceUsd = priceUsd,
    date = date
)

fun HistoryCurrencyDbModel.mapToEntity() = HistoryCurrencyEntity(
    id = requireNotNull(id),
    currencyId = currencyId,
    nameId = nameId,
    priceUsd = priceUsd,
    date = date
)

fun HistoryCurrencyEntity.mapToDomain() = HistoryCurrency(
    id = id,
    currencyId = currencyId,
    nameId = nameId,
    priceUsd = priceUsd,
    date = date
)

fun HistoryCurrency.mapToEntity() = HistoryCurrencyEntity(
    id = id,
    currencyId = currencyId,
    nameId = nameId,
    priceUsd = priceUsd,
    date = date
)
