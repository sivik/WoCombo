package com.example.wocombo.app

import android.app.Application
import com.example.wocombo.app.di.appModule
import com.example.wocombo.app.di.coreModule
import com.example.wocombo.app.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModule, networkModule, coreModule)
        }
    }
}