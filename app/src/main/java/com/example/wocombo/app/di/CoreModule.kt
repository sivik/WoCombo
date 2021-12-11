package com.example.wocombo.app.di

import com.example.wocombo.common.broadcast.ScheduleBroadcastManager
import com.example.wocombo.core.data.adapters.remote.datasource.EventsRemoteDataSourceImpl
import com.example.wocombo.core.data.adapters.remote.datasource.ScheduleRemoteDataSourceImpl
import com.example.wocombo.core.data.datasources.remote.EventsRemoteDataSource
import com.example.wocombo.core.data.datasources.remote.ScheduleRemoteDataSource
import com.example.wocombo.core.data.repositories.EventsRepositoryImpl
import com.example.wocombo.core.data.repositories.ScheduleRepositoryImpl
import com.example.wocombo.core.domain.repositories.EventsRepository
import com.example.wocombo.core.domain.repositories.ScheduleRepository
import com.example.wocombo.core.domain.usecases.DownloadEventsUseCase
import com.example.wocombo.core.domain.usecases.DownloadSchedulesUseCase
import com.example.wocombo.core.domain.usecases.LoginUseCase
import com.example.wocombo.core.presentation.ui.login.LoginViewModel
import com.example.wocombo.core.presentation.ui.playback.PlaybackViewModel
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

    /*USECASE*/
    single { DownloadSchedulesUseCase(get()) }
    single { DownloadEventsUseCase(get()) }
    single { LoginUseCase() }

    /*REPOSITORY*/
    single<EventsRepository> { EventsRepositoryImpl(get()) }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get()) }

    /*DATASOURCE*/
    single<EventsRemoteDataSource> { EventsRemoteDataSourceImpl(get()) }
    single<ScheduleRemoteDataSource> { ScheduleRemoteDataSourceImpl(get()) }

    /*BROADCAST*/
    single { ScheduleBroadcastManager() }

}