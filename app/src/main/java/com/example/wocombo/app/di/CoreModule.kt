package com.example.wocombo.app.di

import com.example.wocombo.common.broadcast.ScheduleBroadcastManager
import com.example.wocombo.core.data.adapters.database.datasource.ReminderDbDataSourceImpl
import com.example.wocombo.core.data.adapters.remote.datasource.EventsRemoteDataSourceImpl
import com.example.wocombo.core.data.adapters.remote.datasource.ScheduleRemoteDataSourceImpl
import com.example.wocombo.core.data.datasources.database.ReminderDbDataSource
import com.example.wocombo.core.data.datasources.remote.EventsRemoteDataSource
import com.example.wocombo.core.data.datasources.remote.ScheduleRemoteDataSource
import com.example.wocombo.core.data.repositories.EventsRepositoryImpl
import com.example.wocombo.core.data.repositories.ReminderRepositoryImpl
import com.example.wocombo.core.data.repositories.ScheduleRepositoryImpl
import com.example.wocombo.core.domain.repositories.EventsRepository
import com.example.wocombo.core.domain.repositories.ReminderRepository
import com.example.wocombo.core.domain.repositories.ScheduleRepository
import com.example.wocombo.core.domain.usecases.*
import com.example.wocombo.core.presentation.ui.login.LoginViewModel
import com.example.wocombo.core.presentation.ui.playback.PlaybackViewModel
import com.example.wocombo.core.presentation.ui.reminders.RemindersViewModel
import com.example.wocombo.core.presentation.ui.transmissionlist.TransmissionListViewModel
import com.example.wocombo.core.presentation.ui.transmissionlist.events.EventListViewModel
import com.example.wocombo.core.presentation.ui.transmissionlist.schedules.ScheduleListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {

    /*VIEWMODEL*/
    viewModel { LoginViewModel(get()) }
    viewModel { PlaybackViewModel() }
    viewModel { EventListViewModel(get()) }
    viewModel { ScheduleListViewModel(get()) }
    viewModel { TransmissionListViewModel() }
    viewModel { RemindersViewModel(get(),get(),get()) }

    /*USECASE*/
    single { DownloadSchedulesUseCase(get()) }
    single { DownloadEventsUseCase(get()) }
    single { LoginUseCase() }
    single { AddReminderUseCase(get()) }
    single { DeleteReminderUseCase(get()) }
    single { GetAllRemindersUseCase(get()) }

    /*REPOSITORY*/
    single<EventsRepository> { EventsRepositoryImpl(get()) }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get()) }
    single<ReminderRepository> { ReminderRepositoryImpl(get()) }

    /*DATASOURCE*/
    single<EventsRemoteDataSource> { EventsRemoteDataSourceImpl(get()) }
    single<ScheduleRemoteDataSource> { ScheduleRemoteDataSourceImpl(get()) }
    single<ReminderDbDataSource> { ReminderDbDataSourceImpl(get()) }

    /*BROADCAST*/
    single { ScheduleBroadcastManager() }

}