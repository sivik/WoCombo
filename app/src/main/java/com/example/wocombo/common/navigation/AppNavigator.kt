package com.example.wocombo.common.navigation

import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.wocombo.R
import com.example.wocombo.common.logs.LoggerTags

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

    override fun openTransmissionList() {
        Log.d(LoggerTags.NAVIGATOR, "Navigate from login to transmission list")
        navController?.navigate(R.id.action_loginFragment_to_navigation_transmission_list)
    }

    override fun openStream(url: String) {
        Log.d(LoggerTags.NAVIGATOR, "Navigate from transmission list to video stream")
        val bundle = bundleOf(
            Pair(NavigationConst.VIDEO_URL, url)
        )
        navController?.navigate(R.id.action_navigation_transmission_list_to_streamFragment, bundle)
    }

    override fun openAboutApp() {
        Log.d(LoggerTags.NAVIGATOR, "Navigate from transmission list to about app")
        navController?.navigate(R.id.action_navigation_transmission_list_to_aboutFragment)
    }


}