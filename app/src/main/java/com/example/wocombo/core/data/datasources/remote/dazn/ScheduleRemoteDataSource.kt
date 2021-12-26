package com.example.wocombo.core.data.datasources.remote.dazn

import com.example.wocombo.core.data.model.ScheduleEntity

interface ScheduleRemoteDataSource {
    fun downloadSchedules(): List<ScheduleEntity>
}