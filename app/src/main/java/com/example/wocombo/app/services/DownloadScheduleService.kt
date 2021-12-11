package com.example.wocombo.app.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.core.domain.repositories.ScheduleRepository
import com.example.wocombo.common.broadcast.ScheduleBroadcastManager
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DownloadScheduleService : KoinComponent, Service() {

    private val repository: ScheduleRepository = get()
    private val broadcastManager: ScheduleBroadcastManager = get()
    private lateinit var job: Job

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LoggerTags.DOWNLOAD_SCHEDULE_SERVICE, "Start Download schedule service")
        job = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(30000)
                handleSchedulesFromServer()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(LoggerTags.DOWNLOAD_SCHEDULE_SERVICE, "Stop Download schedule service")
        if (::job.isInitialized) {
            Log.d(LoggerTags.DOWNLOAD_SCHEDULE_SERVICE, "Stop Download schedule service Coroutine")
            job.cancel()
        } else {
            Log.w(
                LoggerTags.DOWNLOAD_SCHEDULE_SERVICE,
                "Cannot destroy download schedule service  because is not initialized"
            )
        }
        super.onDestroy()
    }

    private fun handleSchedulesFromServer() {
        Log.d(LoggerTags.DOWNLOAD_SCHEDULE_SERVICE, "Trying download schedule from remote server.")
        try {
            val schedules = repository.downloadSchedules()
            broadcastManager.assignSchedules(schedules, this@DownloadScheduleService)
        } catch (e: Exception) {
            Log.e(
                LoggerTags.DOWNLOAD_SCHEDULE_SERVICE,
                "Unexpected error while download schedules from Download schedule service.",
                e
            )
        }
    }
}