package com.example.wocombo.core.data.datasources.database

import androidx.paging.PagingData
import com.example.wocombo.core.data.model.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface ReminderDbDataSource {
    fun addReminder(entity: ReminderEntity)
    fun deleteReminder(id: Int)
    fun getRemindersStream(): Flow<PagingData<ReminderEntity>>
}