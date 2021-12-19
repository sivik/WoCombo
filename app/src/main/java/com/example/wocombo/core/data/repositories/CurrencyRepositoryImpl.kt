package com.example.wocombo.core.data.repositories

import android.util.Log
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.core.data.datasources.database.HistoryCurrencyDbDataSource
import com.example.wocombo.core.data.datasources.remote.twoupdigital.CurrencyRemoteDataSource
import com.example.wocombo.core.data.mappers.mapToDomain
import com.example.wocombo.core.data.mappers.mapToEntity
import com.example.wocombo.core.domain.enums.Indicator
import com.example.wocombo.core.domain.models.Currency
import com.example.wocombo.core.domain.models.HistoryCurrency
import com.example.wocombo.core.domain.repositories.CurrencyRepository
import org.joda.time.DateTime
import kotlin.math.pow

class CurrencyRepositoryImpl(
    private val remoteDataSource: CurrencyRemoteDataSource,
    private val dbDataSource: HistoryCurrencyDbDataSource
) : CurrencyRepository {

    override fun getCurrencies(): List<Currency> {
        val downloadedCurrencies = remoteDataSource.downloadCurrencies().map { it.mapToDomain() }
        val filledCurrencies = downloadedCurrencies.map { currency ->
            val historyCurrencies =
                dbDataSource.getHistoryCurrencyInfo(currency.id).map { it.mapToDomain() }
            currency.indicator = calculateIndicator(currency, ArrayList(historyCurrencies))
            currency
        }
        dbDataSource.insert(downloadedCurrencies.map {
            HistoryCurrency(
                id = null,
                currencyId = it.id,
                nameId = it.nameId,
                priceUsd = it.priceUsd,
                date = DateTime.now()
            ).mapToEntity()
        })

        return filledCurrencies
    }

    private fun calculateIndicator(
        actualCurrency: Currency,
        historyCurrency: ArrayList<HistoryCurrency>
    ): Indicator {

        historyCurrency.add(
            HistoryCurrency(
                id = null,
                currencyId = actualCurrency.id,
                priceUsd = actualCurrency.priceUsd,
                date = DateTime.now(),
                nameId = actualCurrency.nameId
            )
        )
        historyCurrency.sortBy { it.date.millis }

        if (historyCurrency.size == 1) return Indicator.EQUALS

        var nominator = 0.0
        var denominator = 0.0

        var actualXSumNominator = 0.0
        var actualYSumNominator = 0.0
        var actualXSumDenominator = 0.0
        var averageSumDenominator = 0.0

        historyCurrency.takeLast(20).forEachIndexed { index, curr ->
            val movedIndex = index + 1

            actualXSumNominator = (actualXSumNominator + movedIndex)
            val averageXNom: Double =  actualXSumNominator / movedIndex

            actualYSumNominator = (actualYSumNominator + curr.priceUsd)
            val averageYNom: Double =  actualYSumNominator / movedIndex

            actualXSumDenominator = (actualXSumDenominator + movedIndex)
            val averageXDenom: Double =  actualXSumDenominator / movedIndex
            averageSumDenominator += (movedIndex - averageXDenom)

            nominator += (curr.priceUsd - averageYNom) * (movedIndex - averageXNom)
        }

        denominator += 2.0.pow(averageSumDenominator)

        if (denominator == 0.0) return Indicator.EQUALS

        val slope = nominator / denominator

        Log.d(
            LoggerTags.INDICATOR,
            "Indicator for currency: ${historyCurrency.firstOrNull()?.nameId} -> Slope: $slope"
        )

        return when {
            slope < 0 -> Indicator.DECREASE
            slope > 0 -> Indicator.INCREASE
            slope == 0.0 -> Indicator.EQUALS
            else -> Indicator.EQUALS
        }
    }
}