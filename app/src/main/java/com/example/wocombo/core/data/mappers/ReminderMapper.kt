package com.example.wocombo.core.data.mappers

import com.example.wocombo.core.data.model.ReminderEntity
import com.example.wocombo.core.domain.models.Reminder
import com.example.wocombo.database.data.model.ReminderDbModel

fun ReminderDbModel.mapToEntity() = ReminderEntity(
    id = id,
    title = title,
    date = date
)

fun ReminderEntity.mapToDomain() = Reminder(
    id = id,
    title = title,
    date = date
)

fun Reminder.mapToEntity() = ReminderEntity(
    id = id,
    title = title,
    date = date
)


fun ReminderEntity.mapToDbModel() = ReminderDbModel(
    id = id,
    title = title,
    date = date
)