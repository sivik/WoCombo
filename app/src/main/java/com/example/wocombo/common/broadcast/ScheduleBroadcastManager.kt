package com.example.wocombo.common.broadcast

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.wocombo.core.domain.models.Schedule
import org.koin.core.component.KoinComponent


class ScheduleBroadcastManager: KoinComponent {

    private lateinit var broadcaster: LocalBroadcastManager

    fun assignSchedules(schedules: List<Schedule>, context: Context) {
        broadcaster = LocalBroadcastManager.getInstance(context)
        val intent =
            Intent(ScheduleBroadcastConst.SCHEDULE_INTENT).apply {
                putParcelableArrayListExtra(ScheduleBroadcastConst.SCHEDULES, ArrayList(schedules))
            }
        broadcaster.sendBroadcast(intent)
    }
}