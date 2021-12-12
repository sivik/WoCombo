package com.example.wocombo.core.data.repositories

import androidx.paging.PagingData
import androidx.paging.map
import com.example.wocombo.core.data.datasources.database.ReminderDbDataSource
import com.example.wocombo.core.data.mappers.mapToDomain
import com.example.wocombo.core.data.mappers.mapToEntity
import com.example.wocombo.core.domain.models.Reminder
import com.example.wocombo.core.domain.repositories.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class ReminderRepositoryImpl(
    private val dataSource: ReminderDbDataSource
) : ReminderRepository {
    override fun addReminder(reminder: Reminder) = dataSource.addReminder(reminder.mapToEntity())

    override fun deleteReminder(id: Int) = dataSource.deleteReminder(id)

    override fun getRemindersStream(): Flow<PagingData<Reminder>> {
        return dataSource.getRemindersStream().transform { flowCollector ->
                emit(flowCollector.map { reminder -> reminder.mapToDomain() })
            }
    }

}