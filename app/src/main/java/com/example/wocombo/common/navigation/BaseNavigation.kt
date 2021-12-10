package com.example.wocombo.common.navigation

import androidx.navigation.NavController

interface BaseNavigation {

    fun getNavigator(): NavController?

    fun bind(navController: NavController)

    fun popBackStack()

}