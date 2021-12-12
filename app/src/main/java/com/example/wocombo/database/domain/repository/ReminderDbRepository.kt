package com.example.wocombo.database.domain.repository

import androidx.paging.PagingSource
import com.example.wocombo.database.data.model.ReminderDbModel

interface ReminderDbRepository {
    fun insert(model: ReminderDbModel)
    fun getAll(): PagingSource<Int, ReminderDbModel>
    fun deleteById(id: Int)
}