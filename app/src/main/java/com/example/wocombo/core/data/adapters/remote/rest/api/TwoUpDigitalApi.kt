package com.example.wocombo.core.data.adapters.remote.rest.api

import com.example.wocombo.common.network.TwoUpDigitalConstants
import com.example.wocombo.core.data.adapters.remote.rest.response.CurrenciesDataRemoteModel
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApi {
    @GET(TwoUpDigitalConstants.TICKERS)
    fun downloadCurrencies(): Call<CurrenciesDataRemoteModel>
}