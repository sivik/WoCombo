package com.example.wocombo.core.data.repositories

import com.example.wocombo.core.data.datasources.remote.ScheduleRemoteDataSource
import com.example.wocombo.core.data.mappers.mapToDomain
import com.example.wocombo.core.domain.models.Schedule
import com.example.wocombo.core.domain.repositories.ScheduleRepository

class ScheduleRepositoryImpl(
    private val dataSource: ScheduleRemoteDataSource
) : ScheduleRepository {

    override fun downloadSchedules(): List<Schedule> =
        dataSource.downloadSchedules().map { it.mapToDomain() }

}