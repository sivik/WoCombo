package com.example.wocombo.core.data.datasources.database

import com.example.wocombo.core.data.model.HistoryCurrencyEntity

interface HistoryCurrencyDbDataSource {
    fun insert(entities: List<HistoryCurrencyEntity>): Array<Long>
    fun getHistoryCurrencyInfo(currencyId: Int): List<HistoryCurrencyEntity>
}
