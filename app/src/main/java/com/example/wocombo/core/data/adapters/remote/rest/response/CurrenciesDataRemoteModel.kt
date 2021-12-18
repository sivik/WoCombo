package com.example.wocombo.core.data.adapters.remote.rest.response

data class CurrenciesDataRemoteModel(
    val `data`: List<CurrencyRemoteModel>,
    val dataInfo: CurrencyDataInfoRemoteModel
)

data class CurrencyDataInfoRemoteModel(
    val coins_num: Int,
    val time: Int
)

data class CurrencyRemoteModel(
    val csupply: String,
    val id: String,
    val market_cap_usd: String,
    val msupply: String,
    val name: String,
    val nameid: String,
    val percent_change_1h: String,
    val percent_change_24h: String,
    val percent_change_7d: String,
    val price_btc: String,
    val price_usd: String,
    val rank: Int,
    val symbol: String,
    val tsupply: String,
    val volume24: Double,
    val volume24a: Double
)