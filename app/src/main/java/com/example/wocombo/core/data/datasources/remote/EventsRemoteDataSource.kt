package com.example.wocombo.core.data.datasources.remote

import com.example.wocombo.core.data.model.EventEntity

interface EventsRemoteDataSource {
    fun downloadEvents(): List<EventEntity>
}