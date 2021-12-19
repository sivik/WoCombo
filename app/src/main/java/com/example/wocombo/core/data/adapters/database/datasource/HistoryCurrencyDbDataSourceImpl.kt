package com.example.wocombo.core.data.adapters.database.datasource

import com.example.wocombo.core.data.datasources.database.HistoryCurrencyDbDataSource
import com.example.wocombo.core.data.mappers.mapToDbModel
import com.example.wocombo.core.data.mappers.mapToEntity
import com.example.wocombo.core.data.model.HistoryCurrencyEntity
import com.example.wocombo.database.domain.repository.HistoryCurrencyDbRepository

class HistoryCurrencyDbDataSourceImpl(
    private val historyCurrencyDbRepository: HistoryCurrencyDbRepository
) : HistoryCurrencyDbDataSource {

    override fun insert(entities: List<HistoryCurrencyEntity>) =
        historyCurrencyDbRepository.insert(entities.map { it.mapToDbModel() })

    override fun getHistoryCurrencyInfo(currencyId: Int): List<HistoryCurrencyEntity> =
        historyCurrencyDbRepository.getAllHistoryByCurrencyId(currencyId).map { it.mapToEntity() }

}