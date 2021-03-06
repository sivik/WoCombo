package com.example.wocombo.app.services

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.wocombo.common.logs.LoggerTags
import com.example.wocombo.common.navigation.BaseNavigation
import kotlin.reflect.KClass

class ServiceManager {

    companion object {

        fun startDownloadService(context: Context, navigator: BaseNavigation, clazz: KClass<*>) {

            val isNotCurrentLoginFragment =
                navigator.getNavigator()?.currentDestination?.label != "Login"

            if (isNotCurrentLoginFragment) {
                val activityManager =
                    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                    if (clazz.java.name == service.service.className) {
                        return
                    }
                }
                Log.d(LoggerTags.DOWNLOAD_SCHEDULE_SERVICE, "Start Download Service from activity")
                context.startService(Intent(context, clazz.java))
            }
        }

        fun stopDownloadService(context: Context, clazz: KClass<*>) {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (DownloadScheduleService::class.java.name == service.service.className) {
                    Log.d(LoggerTags.DOWNLOAD_SCHEDULE_SERVICE, "Stop Download Service from activity")
                    context.stopService(Intent(context, clazz.java))
                }
            }
        }
    }
}