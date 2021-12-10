package com.example.wocombo.app.di

import com.example.wocombo.common.navigation.AppNavigation
import com.example.wocombo.common.navigation.BaseNavigation
import org.koin.dsl.module

val appModule = module {
    single<BaseNavigation> { AppNavigation() }
}