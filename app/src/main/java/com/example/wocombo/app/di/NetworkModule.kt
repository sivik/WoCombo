package com.example.wocombo.app.di

import com.example.wocombo.common.network.provideAnonymousRetrofit
import com.example.wocombo.core.data.adapters.remote.rest.api.EventApi
import com.example.wocombo.core.data.adapters.remote.rest.api.ScheduleApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

object NetworkRetrofit {
    const val ANONYMOUS = "anonymous"
}

val networkModule = module {
    single(named(NetworkRetrofit.ANONYMOUS)) { provideAnonymousRetrofit(get()) }
    single { provideApi<EventApi>(get(named(NetworkRetrofit.ANONYMOUS))) }
    single { provideApi<ScheduleApi>(get(named(NetworkRetrofit.ANONYMOUS))) }
}

inline fun <reified T> provideApi(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}
