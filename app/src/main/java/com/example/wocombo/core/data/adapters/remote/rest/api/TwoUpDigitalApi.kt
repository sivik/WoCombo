package com.example.wocombo.core.data.adapters.remote.rest.api

import com.example.wocombo.common.network.TwoUpDigitalConstants
import com.example.wocombo.core.data.adapters.remote.rest.response.CurrenciesDataRemoteModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET(TwoUpDigitalConstants.TICKERS)
    fun downloadCurrencies(@Query("limit") limit: Int = 20): Call<CurrenciesDataRemoteModel>
}