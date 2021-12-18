package com.example.wocombo.core.domain.repositories

import com.example.wocombo.core.domain.models.Currency

interface CurrencyRepository {
    fun downloadCurrencies(): List<Currency>
}