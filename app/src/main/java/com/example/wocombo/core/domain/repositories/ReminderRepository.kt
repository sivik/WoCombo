package com.example.wocombo.core.domain.repositories

import androidx.paging.PagingData
import com.example.wocombo.core.domain.models.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun addReminder(reminder: Reminder)
    fun deleteReminder(id: Int)
    fun getRemindersStream(): Flow<PagingData<Reminder>>?
}