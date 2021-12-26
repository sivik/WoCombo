package com.example.wocombo.core.data.adapters.remote.datasource.twoupdigital

import android.util.Log
import com.example.wocombo.common.logs.LoggerHelper
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.core.data.adapters.remote.rest.api.CurrencyApi
import com.example.wocombo.core.data.datasources.remote.twoupdigital.CurrencyRemoteDataSource
import com.example.wocombo.core.data.exceptions.DownloadCurrencyException
import com.example.wocombo.core.data.mappers.mapToEntity
import com.example.wocombo.core.data.model.CurrencyEntity

class CurrencyRemoteDataSourceImpl(
    private val api: CurrencyApi
) : CurrencyRemoteDataSource {

    private val tag = LoggerTags.CURRENCY

    override fun downloadCurrencies(): List<CurrencyEntity> {
        Log.d(tag, "Start download currency list")
        val response = api.downloadCurrencies().execute()
        if (response.isSuccessful) {
            Log.d(tag, "Currency list download successfully")
            return response.body()?.data?.map { it.mapToEntity() }
                ?: throw DownloadCurrencyException("No body in response from server")
        } else {
            LoggerHelper.logRemoteError(
                tag,
                response.code(),
                response.message(),
                response.errorBody()?.string() ?: ""
            )
            throw DownloadCurrencyException("Cannot download currency list from server")
        }
    }
}