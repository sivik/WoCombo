package com.example.wocombo.database.data.repository

import androidx.paging.PagingSource
import com.example.wocombo.database.data.dao.ReminderDao
import com.example.wocombo.database.data.model.ReminderDbModel
import com.example.wocombo.database.domain.repository.ReminderDbRepository

class ReminderDbRepositoryImpl(
    private val reminderDao: ReminderDao
): ReminderDbRepository {

    override fun insert(model: ReminderDbModel) = reminderDao.insert(model)

    override fun getAll(): PagingSource<Int, ReminderDbModel> = reminderDao.getAll()

    override fun deleteById(id: Int) = reminderDao.deleteById(id)
}