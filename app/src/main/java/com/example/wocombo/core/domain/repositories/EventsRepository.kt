package com.example.wocombo.core.domain.repositories

import com.example.wocombo.core.domain.models.Event


interface EventsRepository {
    fun downloadEvents(): List<Event>
}