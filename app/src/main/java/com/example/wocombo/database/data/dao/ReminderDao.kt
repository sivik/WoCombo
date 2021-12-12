package com.example.wocombo.database.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wocombo.database.data.model.ReminderDbModel

@Dao
interface ReminderDao : BaseDao<ReminderDbModel> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: ReminderDbModel)

    @Query("SELECT * FROM reminders")
    fun getAll(): PagingSource<Int, ReminderDbModel>

    @Query("DELETE FROM reminders WHERE id = :id")
    fun deleteById(id: Int)
}