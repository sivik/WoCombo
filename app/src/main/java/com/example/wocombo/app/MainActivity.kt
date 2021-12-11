package com.example.wocombo.app

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wocombo.R
import com.example.wocombo.databinding.ActivityMainBinding
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.navigation.BackPressFragment
import com.example.wocombo.common.navigation.BaseNavigation
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val binding by viewInflateBinding(ActivityMainBinding::inflate)
    private val navigation by inject<BaseNavigation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navigation.bind(navController)
    }

    override fun onBackPressed() {
        val currentFragment =
            supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.first()
        if (currentFragment !is BackPressFragment || !currentFragment.onBackPressed())
            super.onBackPressed()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment_activity_main).navigateUp()

}