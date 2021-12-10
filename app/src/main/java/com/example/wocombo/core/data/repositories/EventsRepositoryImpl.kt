package com.example.wocombo.core.data.repositories

import com.example.wocombo.core.data.datasources.remote.EventsRemoteDataSource
import com.example.wocombo.core.data.mappers.mapToDomain
import com.example.wocombo.core.domain.models.Event
import com.example.wocombo.core.domain.repositories.EventsRepository

class EventsRepositoryImpl(
    private val dataSource: EventsRemoteDataSource
) : EventsRepository {

    override fun downloadEvents(): List<Event> =
        dataSource.downloadEvents().map { it.mapToDomain() }

}