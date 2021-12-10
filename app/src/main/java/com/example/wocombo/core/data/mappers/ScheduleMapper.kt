package com.example.wocombo.core.data.mappers

import com.example.wocombo.core.data.adapters.remote.rest.response.ScheduleItemRemoteModel
import com.example.wocombo.core.data.model.ScheduleEntity
import com.example.wocombo.core.domain.models.Schedule

fun ScheduleItemRemoteModel.mapToEntity() = ScheduleEntity(
    date = date,
    id = id,
    imageUrl = imageUrl,
    subtitle = subtitle,
    title = title
)

fun ScheduleEntity.mapToDomain() = Schedule(
    date = date,
    id = id,
    imageUrl = imageUrl,
    subtitle = subtitle,
    title = title
)