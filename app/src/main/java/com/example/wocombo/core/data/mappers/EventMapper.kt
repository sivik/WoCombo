package com.example.wocombo.core.data.mappers

import com.example.wocombo.core.data.adapters.remote.rest.response.EventsItemRemoteModel
import com.example.wocombo.core.data.model.EventEntity
import com.example.wocombo.core.domain.models.Event

fun EventsItemRemoteModel.mapToEntity() = EventEntity(
    date = date,
    id = id,
    imageUrl = imageUrl,
    subtitle = subtitle,
    title = title,
    videoUrl = videoUrl
)

fun EventEntity.mapToDomain() = Event(
    date = date,
    id = id,
    imageUrl = imageUrl,
    subtitle = subtitle,
    title = title,
    videoUrl = videoUrl
)