package com.example.wocombo.app.di

import com.example.wocombo.common.broadcast.CurrencyBroadcastManager
import com.example.wocombo.common.broadcast.ScheduleBroadcastManager
import com.example.wocombo.core.data.adapters.database.datasource.ReminderDbDataSourceImpl
import com.example.wocombo.core.data.adapters.remote.datasource.dazn.EventsRemoteDataSourceImpl
import com.example.wocombo.core.data.adapters.remote.datasource.dazn.ScheduleRemoteDataSourceImpl
import com.example.wocombo.core.data.adapters.remote.datasource.twoupdigital.CurrencyRemoteDataSourceImpl
import com.example.wocombo.core.data.datasources.database.ReminderDbDataSource
import com.example.wocombo.core.data.datasources.remote.dazn.EventsRemoteDataSource
import com.example.wocombo.core.data.datasources.remote.dazn.ScheduleRemoteDataSource
import com.example.wocombo.core.data.datasources.remote.twoupdigital.CurrencyRemoteDataSource
import com.example.wocombo.core.data.repositories.CurrencyRepositoryImpl
import com.example.wocombo.core.data.repositories.EventsRepositoryImpl
import com.example.wocombo.core.data.repositories.ReminderRepositoryImpl
import com.example.wocombo.core.data.repositories.ScheduleRepositoryImpl
import com.example.wocombo.core.domain.repositories.CurrencyRepository
import com.example.wocombo.core.domain.repositories.EventsRepository
import com.example.wocombo.core.domain.repositories.ReminderRepository
import com.example.wocombo.core.domain.repositories.ScheduleRepository
import com.example.wocombo.core.domain.usecases.*
import com.example.wocombo.core.presentation.ui.transmissionlist.currencies.CurrencyListViewModel
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
    viewModel { CurrencyListViewModel(get()) }

    /*USECASE*/
    single { DownloadSchedulesUseCase(get()) }
    single { DownloadEventsUseCase(get()) }
    single { LoginUseCase() }
    single { AddReminderUseCase(get()) }
    single { DeleteReminderUseCase(get()) }
    single { GetAllRemindersUseCase(get()) }
    single { DownloadCurrenciesUseCase(get()) }

    /*REPOSITORY*/
    single<EventsRepository> { EventsRepositoryImpl(get()) }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get()) }
    single<ReminderRepository> { ReminderRepositoryImpl(get()) }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }

    /*DATASOURCE*/
    single<EventsRemoteDataSource> { EventsRemoteDataSourceImpl(get()) }
    single<ScheduleRemoteDataSource> { ScheduleRemoteDataSourceImpl(get()) }
    single<CurrencyRemoteDataSource> { CurrencyRemoteDataSourceImpl(get()) }
    single<ReminderDbDataSource> { ReminderDbDataSourceImpl(get()) }

    /*BROADCAST*/
    single { ScheduleBroadcastManager() }
    single { CurrencyBroadcastManager() }

}