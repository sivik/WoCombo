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

    override fun openAboutApp() {
        Log.d(LoggerTags.NAVIGATOR, "Navigate from transmission list to about app")
        navController?.navigate(R.id.action_navigation_transmission_list_to_aboutFragment)
    }

    override fun openPlayback(title: String, videoUrl: String) {
        Log.d(LoggerTags.NAVIGATOR, "Navigate from event list to playback")
        val bundle = bundleOf(
            Pair(NavigationConst.VIDEO_URL, videoUrl),
            Pair(NavigationConst.VIDEO_TITLE, videoUrl)
        )
        navController?.navigate(R.id.action_navigation_transmission_list_to_streamFragment, bundle)
    }

    override fun openRemainders() {
        Log.d(LoggerTags.NAVIGATOR, "Navigate from transmission list to reminders")
        navController?.navigate(R.id.action_navigation_transmission_list_to_remindersFragment)
    }
}