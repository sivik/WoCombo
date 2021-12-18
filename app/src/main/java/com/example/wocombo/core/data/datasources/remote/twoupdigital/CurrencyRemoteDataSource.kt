package com.example.wocombo.core.data.datasources.remote.twoupdigital

import com.example.wocombo.core.data.model.CurrencyEntity

interface CurrencyRemoteDataSource {
    fun downloadCurrencies(): List<CurrencyEntity>
}