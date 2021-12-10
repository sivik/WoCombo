package com.example.wocombo.core.domain.repositories

import com.example.wocombo.core.domain.models.Schedule


interface ScheduleRepository {
    fun downloadSchedules(): List<Schedule>
}