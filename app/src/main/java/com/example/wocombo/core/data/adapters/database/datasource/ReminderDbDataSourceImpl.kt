package com.example.wocombo.core.data.adapters.database.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.wocombo.core.data.datasources.database.ReminderDbDataSource
import com.example.wocombo.core.data.mappers.mapToDbModel
import com.example.wocombo.core.data.mappers.mapToEntity
import com.example.wocombo.core.data.model.ReminderEntity
import com.example.wocombo.database.domain.repository.ReminderDbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class ReminderDbDataSourceImpl(
    private val reminderDbRepository: ReminderDbRepository,
) : ReminderDbDataSource {
    override fun addReminder(entity: ReminderEntity) =
        reminderDbRepository.insert(entity.mapToDbModel())

    override fun deleteReminder(id: Int) = reminderDbRepository.deleteById(id)

    override fun getRemindersStream(): Flow<PagingData<ReminderEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = false)
        ) {
            reminderDbRepository.getAll()
        }.flow.transform { flowCollector ->
            emit(flowCollector.map { reminder -> reminder.mapToEntity() })
        }
    }
}