package com.example.wocombo.common.network

import com.example.wocombo.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideAnonymousRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        //.baseUrl(BuildConfig.BASE_ANONYMOUS_URL)
        .baseUrl("https://us-central1-dazn-sandbox.cloudfunctions.net/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
        .build()
}