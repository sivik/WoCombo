package com.example.wocombo.common.network

import com.example.wocombo.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun provideAnonymousRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        //.baseUrl(BuildConfig.BASE_ANONYMOUS_URL)
        .baseUrl("https://us-central1-dazn-sandbox.cloudfunctions.net/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
        .build()
}

fun provideAnonymousOkHttpClient(): OkHttpClient {
    val authInterceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .build()
        chain.proceed(request)
    }

    return getUnsafeOkHttpClient()
        .addInterceptor(authInterceptor)
        .build()
}

fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }
    })

    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
    val sslSocketFactory = sslContext.socketFactory

    val builder = OkHttpClient.Builder()
    builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
    builder.hostnameVerifier { _, _ -> true }
    return builder
}