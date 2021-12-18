package com.example.wocombo.app.di

import com.example.wocombo.common.network.provideAnonymousOkHttpClient
import com.example.wocombo.common.network.provideAnonymousDaznRetrofit
import com.example.wocombo.common.network.provideAnonymousTwoUpRetrofit
import com.example.wocombo.core.data.adapters.remote.rest.api.CurrencyApi
import com.example.wocombo.core.data.adapters.remote.rest.api.EventApi
import com.example.wocombo.core.data.adapters.remote.rest.api.ScheduleApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

object NetworkRetrofit {
    const val DAZN_ANONYMOUS = "DAZN_ANONYMOUS"
    const val TWO_UP_ANONYMOUS = "ANONYMOUS"
}

object ClientNames {
    const val ANONYMOUS = "CLIENT_ANONYMOUS"
}

val networkModule = module {

    single(named(ClientNames.ANONYMOUS)) { provideAnonymousOkHttpClient() }
    single(named(NetworkRetrofit.DAZN_ANONYMOUS)) { provideAnonymousDaznRetrofit(get(named(ClientNames.ANONYMOUS))) }
    single(named(NetworkRetrofit.TWO_UP_ANONYMOUS)) { provideAnonymousTwoUpRetrofit(get(named(ClientNames.ANONYMOUS))) }

    single { provideApi<EventApi>(get(named(NetworkRetrofit.DAZN_ANONYMOUS))) }
    single { provideApi<ScheduleApi>(get(named(NetworkRetrofit.DAZN_ANONYMOUS))) }
    single { provideApi<CurrencyApi>(get(named(NetworkRetrofit.TWO_UP_ANONYMOUS))) }
}

inline fun <reified T> provideApi(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}
