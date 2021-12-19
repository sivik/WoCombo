package com.example.wocombo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wocombo.database.data.converters.DateConverter
import com.example.wocombo.database.data.dao.HistoryCurrencyDao
import com.example.wocombo.database.data.dao.ReminderDao
import com.example.wocombo.database.data.model.HistoryCurrencyDbModel
import com.example.wocombo.database.data.model.ReminderDbModel

@Database(
    entities = [
        ReminderDbModel::class,
        HistoryCurrencyDbModel::class,
    ],
    version = 1,
    exportSchema = true
)

@TypeConverters(
    DateConverter::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun historyCurrencyDao(): HistoryCurrencyDao
}