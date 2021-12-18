package com.example.wocombo.core.data.repositories

import com.example.wocombo.core.data.datasources.remote.twoupdigital.CurrencyRemoteDataSource
import com.example.wocombo.core.data.mappers.mapToDomain
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.repositories.CurrencyRepository

class CurrencyRepositoryImpl (
    private val dataSource: CurrencyRemoteDataSource
) : CurrencyRepository {

    override fun downloadCurrencies(): List<Currency> =
        dataSource.downloadCurrencies().map { it.mapToDomain() }

}