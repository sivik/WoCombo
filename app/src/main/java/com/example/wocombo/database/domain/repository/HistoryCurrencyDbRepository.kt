package com.example.wocombo.database.domain.repository

import com.example.wocombo.database.data.model.HistoryCurrencyDbModel

interface HistoryCurrencyDbRepository {
    fun insert(models: List<HistoryCurrencyDbModel>): Array<Long>
    fun getAllHistoryByCurrencyId(currencyId:Int): List<HistoryCurrencyDbModel>
}