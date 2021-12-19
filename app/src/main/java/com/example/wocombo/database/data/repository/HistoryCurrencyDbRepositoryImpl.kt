package com.example.wocombo.database.data.repository

import com.example.wocombo.database.data.dao.HistoryCurrencyDao
import com.example.wocombo.database.data.model.HistoryCurrencyDbModel
import com.example.wocombo.database.domain.repository.HistoryCurrencyDbRepository

class HistoryCurrencyDbRepositoryImpl(
    private val historyCurrencyDao: HistoryCurrencyDao
) : HistoryCurrencyDbRepository {

    override fun insert(models: List<HistoryCurrencyDbModel>) =
        historyCurrencyDao.insert(*models.toTypedArray())

    override fun getAllHistoryByCurrencyId(currencyId: Int): List<HistoryCurrencyDbModel> =
        historyCurrencyDao.getAllHistoryByCurrencyId(currencyId)
}