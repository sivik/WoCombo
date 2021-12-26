package com.example.wocombo.app.di

import androidx.room.Room
import com.example.wocombo.core.data.adapters.database.datasource.ReminderDbDataSourceImpl
import com.example.wocombo.core.data.datasources.database.ReminderDbDataSource
import com.example.wocombo.database.AppDatabase
import com.example.wocombo.database.data.repository.HistoryCurrencyDbRepositoryImpl
import com.example.wocombo.database.data.repository.ReminderDbRepositoryImpl
import com.example.wocombo.database.domain.repository.HistoryCurrencyDbRepository
import com.example.wocombo.database.domain.repository.ReminderDbRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object DatabaseConstants{
    const val DATABASE_NAME = "wocombo_db"
}

val databaseModule = module {

    /*Repository*/
    single<ReminderDbRepository> { ReminderDbRepositoryImpl(get()) }
    single<HistoryCurrencyDbRepository> { HistoryCurrencyDbRepositoryImpl(get()) }

    /*DAO*/
    single { get<AppDatabase>().reminderDao() }
    single { get<AppDatabase>().historyCurrencyDao() }

    /*DB*/
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).build()
    }
}
