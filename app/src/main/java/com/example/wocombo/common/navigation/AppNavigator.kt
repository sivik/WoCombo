package com.example.wocombo.common.navigation

import androidx.navigation.NavController


class AppNavigation : BaseNavigation {
    private var navController: NavController? = null

    override fun getNavigator(): NavController? {
        return navController
    }

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun popBackStack() {
        navController?.popBackStack()
    }

}