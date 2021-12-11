package com.example.wocombo.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.wocombo.core.domain.models.Schedule


class ScheduleReceiver(
    private val setUnreadNotificationCountAction: (ArrayList<Schedule>) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val schedules: List<Schedule> =
            intent.getParcelableArrayListExtra(ScheduleBroadcastConst.SCHEDULES) ?: emptyList()
        setUnreadNotificationCountAction.invoke(ArrayList(schedules))
    }
}