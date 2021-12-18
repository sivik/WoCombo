package com.example.wocombo.app.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.wocombo.common.broadcast.CurrencyBroadcastManager
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.core.domain.repositories.CurrencyRepository
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DownloadCurrenciesService : KoinComponent, Service() {

    private val repository: CurrencyRepository = get()
    private val broadcastManager: CurrencyBroadcastManager = get()
    private lateinit var job: Job

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LoggerTags.DOWNLOAD_CURRENCIES_SERVICE, "Start Download currencies service")
        job = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(30000)
                handleSchedulesFromServer()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(LoggerTags.DOWNLOAD_CURRENCIES_SERVICE, "Stop Download currencies service")
        if (::job.isInitialized) {
            Log.d(LoggerTags.DOWNLOAD_CURRENCIES_SERVICE, "Stop Download currencies service Coroutine")
            job.cancel()
        } else {
            Log.w(
                LoggerTags.DOWNLOAD_CURRENCIES_SERVICE,
                "Cannot destroy download currencies service because is not initialized"
            )
        }
        super.onDestroy()
    }

    private fun handleSchedulesFromServer() {
        Log.d(LoggerTags.DOWNLOAD_CURRENCIES_SERVICE, "Trying download currencies from remote server.")
        try {
            val schedules = repository.downloadCurrencies()
            broadcastManager.assignCurrencies(schedules, this@DownloadCurrenciesService)
        } catch (e: Exception) {
            Log.e(
                LoggerTags.DOWNLOAD_CURRENCIES_SERVICE,
                "Unexpected error while download currencies from Download currencies service.",
                e
            )
        }
    }
}