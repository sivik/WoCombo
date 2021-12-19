package com.example.wocombo.core.data.mappers

import com.example.wocombo.core.data.adapters.remote.rest.response.CurrencyRemoteModel
import com.example.wocombo.core.data.model.CurrencyEntity
import com.example.wocombo.core.data.model.EventEntity
import com.example.wocombo.core.data.model.HistoryCurrencyEntity
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.models.HistoryCurrency
import com.example.wocombo.database.data.model.HistoryCurrencyDbModel
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CurrencyMapperTest {

    @Test
    fun `HistoryCurrencyDbModel map to entity`() {
        val model = HistoryCurrencyDbModel(
            id = 1,
            date = DateTime(2020, 2, 2, 3, 4, 5),
            nameId = "doge coin",
            priceUsd = 23.45,
            currencyId = 345
        )

        val expected = HistoryCurrencyEntity(
            id = 1,
            date = DateTime(2020, 2, 2, 3, 4, 5),
            nameId = "doge coin",
            priceUsd = 23.45,
            currencyId = 345
        )

        val result = model.mapToEntity()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `HistoryCurrencyEntity map to domain`() {
        val model = HistoryCurrencyEntity(
            id = 1,
            date = DateTime(2020, 2, 2, 3, 4, 5),
            nameId = "doge coin",
            priceUsd = 23.45,
            currencyId = 345
        )

        val expected = HistoryCurrency(
            id = 1,
            date = DateTime(2020, 2, 2, 3, 4, 5),
            nameId = "doge coin",
            priceUsd = 23.45,
            currencyId = 345
        )
        val result = model.mapToDomain()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `HistoryCurrencyEntity map to dbmodel`() {
        val model = HistoryCurrencyEntity(
            id = 1,
            date = DateTime(2020, 2, 2, 3, 4, 5),
            nameId = "doge coin",
            priceUsd = 23.45,
            currencyId = 345
        )

        val expected = HistoryCurrencyDbModel(
            id = null,
            date = DateTime(2020, 2, 2, 3, 4, 5),
            nameId = "doge coin",
            priceUsd = 23.45,
            currencyId = 345
        )
        val result = model.mapToDbModel()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `HistoryCurrency map to entity`() {
        val model = HistoryCurrency(
            id = 1,
            date = DateTime(2020, 2, 2, 3, 4, 5),
            nameId = "doge coin",
            priceUsd = 23.45,
            currencyId = 345
        )

        val expected = HistoryCurrencyEntity(
            id = 1,
            date = DateTime(2020, 2, 2, 3, 4, 5),
            nameId = "doge coin",
            priceUsd = 23.45,
            currencyId = 345
        )
        val result = model.mapToEntity()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `CurrencyRemoteModel map to entity`() {
        val model = CurrencyRemoteModel(
            csupply = "21312341",
            id = 1,
            market_cap_usd = "12314",
            msupply = "2342352",
            name = "Bitcoin",
            nameid = "bitcoin",
            percent_change_1h = "2.45",
            percent_change_24h = "2.22",
            percent_change_7d = "2.22" ,
            price_btc = "3453.5345",
            price_usd =  3453.5345,
            rank = 14,
            symbol = "BTC",
            tsupply = "21312341",
            volume24 = 123123.2543,
            volume24a = 2356.7867
        )

        val expected = CurrencyEntity(
            id = 1,
            symbol = "BTC",
            name = "Bitcoin",
            nameId = "bitcoin",
            percentHour = "2.45",
            percentDay = "2.22",
            priceUsd = 3453.5345,
            volume = 123123.2543
        )
        val result = model.mapToEntity()
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `CurrencyEntity map to domain`() {
        val model = CurrencyEntity(
            id = 1,
            symbol = "BTC",
            name = "Bitcoin",
            nameId = "bitcoin",
            percentHour = "2.45",
            percentDay = "2.22",
            priceUsd = 3453.5345,
            volume = 123123.2543
        )

        val expected = Currency(
            id = 1,
            symbol = "BTC",
            name = "Bitcoin",
            nameId = "bitcoin",
            percentHour = 2.45,
            percentDay = 2.22,
            priceUsd = 3453.5345,
            volume = 123123.2543
        )
        val result = model.mapToDomain()
        Assertions.assertEquals(expected, result)
    }
}