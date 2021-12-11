package com.example.wocombo.core.presentation.ui.transmissionlist

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.wocombo.R
import com.example.wocombo.app.MainActivity
import com.example.wocombo.app.services.ServiceManager
import com.example.wocombo.common.extensions.reduceDragSensitivity
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.core.presentation.enums.SortType
import com.example.wocombo.core.presentation.helpers.MenuItemsContainer
import com.example.wocombo.databinding.FragmentTransmissionListBinding
import com.google.android.material.navigation.NavigationView
import com.maxkeppeler.sheets.core.SheetStyle
import com.maxkeppeler.sheets.info.InfoSheet
import de.mateware.snacky.Snacky
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.system.exitProcess

class TransmissionListFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by viewInflateBinding(FragmentTransmissionListBinding::inflate)
    private val vm by sharedViewModel<TransmissionListViewModel>()
    private val navigation: BaseNavigation by inject()
    private val menuItemsContainer = MenuItemsContainer()
    private var actionBar: ActionBar? = null
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var actualPagePosition: Int = 0

    private val vp2Callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.bottomNavigationView.menu.getItem(position).isChecked = true
            actualPagePosition = position
            super.onPageSelected(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareActionBar()
        prepareDrawerLayout()
        prepareAdapter()
        prepareBottomNavigation()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        binding.vp2TransmissionList.unregisterOnPageChangeCallback(vp2Callback)
        super.onStop()
    }

    private fun prepareAdapter() {
        val carrierPagerAdapter = TransmissionListAdapter(this)
        binding.vp2TransmissionList.adapter = carrierPagerAdapter
        binding.vp2TransmissionList.reduceDragSensitivity()
        binding.vp2TransmissionList.registerOnPageChangeCallback(vp2Callback)
    }

    private fun prepareBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            binding.vp2TransmissionList.currentItem = when (it.itemId) {
                R.id.navigation_events -> 0
                R.id.navigation_schedule -> 1
                else -> 0
            }
            true
        }
    }

    private fun prepareActionBar() {
        setHasOptionsMenu(true)
        actionBar = (activity as MainActivity).supportActionBar
        actionBar?.let { bar ->
            bar.setDisplayHomeAsUpEnabled(true)
            bar.show()
        }
        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController()
        )
    }

    private fun prepareDrawerLayout() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                showExitDialog()
            }
        }
        drawerToggle = ActionBarDrawerToggle(
            activity,
            binding.drawerLayout,
            R.string.nav_app_bar_open_drawer_description,
            R.string.nav_app_bar_open_drawer_description
        )
        binding.navView.setupWithNavController(findNavController())
        binding.navView.setNavigationItemSelectedListener(this)
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        fun selectDrawerToggle(item: MenuItem) =
            if (drawerToggle.onOptionsItemSelected(item)) true
            else super.onOptionsItemSelected(item)

        fun activateSortMenu(item: MenuItem) {
            item.isCheckable = true
            item.isChecked = true

            if (menuItemsContainer.previousSelectedSortMenuItem != null && menuItemsContainer.previousSelectedSortMenuItem != item) {
                menuItemsContainer.previousSelectedSortMenuItem?.isChecked = false
            }
            menuItemsContainer.previousSelectedSortMenuItem = item
        }

        fun selectActionBar(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_filter -> {
                    Snacky.builder()
                        .setActivity(requireActivity())
                        .setText(R.string.info_filter_not_ready)
                        .setDuration(Snacky.LENGTH_SHORT)
                        .warning()
                        .show()
                    true
                }

                R.id.menu_notification -> {
                    Snacky.builder()
                        .setActivity(requireActivity())
                        .setText(R.string.info_notification_not_ready)
                        .setDuration(Snacky.LENGTH_SHORT)
                        .warning()
                        .show()
                    true
                }
                R.id.menu_sort_date_asc -> {
                    //todo add sorting
                    activateSortMenu(item)
                    vm.setSortType(SortType.ASCENDING)
                    true
                }
                R.id.menu_sort_date_desc -> {
                    //todo add sorting
                    activateSortMenu(item)
                    vm.setSortType(SortType.DESCENDING)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        return when {
            selectDrawerToggle(item) -> true
            selectActionBar(item) -> true
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        fun setSortTypeAndFilters() {
            fun checkWithGettingItem(id: Int): MenuItem {
                val subMenu = menuItemsContainer.sortActionBarMenuItem?.subMenu
                val item = subMenu?.findItem(id)
                item?.isChecked = true
                return requireNotNull(item)
            }

            menuItemsContainer.previousSelectedSortMenuItem = if (vm.sortLiveData.value == null) {
                vm.setSortType(SortType.ASCENDING)
                checkWithGettingItem(R.id.menu_sort_date_asc)
            } else {
                when (vm.sortLiveData.value) {
                    SortType.ASCENDING -> checkWithGettingItem(R.id.menu_sort_date_asc)
                    SortType.DESCENDING -> checkWithGettingItem(R.id.menu_sort_date_desc)
                    else -> checkWithGettingItem(R.id.menu_sort_date_asc)
                }
            }
        }

        inflater.inflate(R.menu.appbar_menu, menu)
        menuItemsContainer.sortActionBarMenuItem = menu.findItem(R.id.menu_sort)
        menuItemsContainer.filtersActionBarMenuItem = menu.findItem(R.id.menu_filter)
        menuItemsContainer.notificationMenuItem = menu.findItem(R.id.menu_notification)
        setSortTypeAndFilters()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_menu_about -> navigation.openAboutApp()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showExitDialog() {
        val ctx = requireContext()
        InfoSheet().show(ctx) {
            style(SheetStyle.DIALOG)
            title(ctx.getString(R.string.exit_title))
            content(ctx.getString(R.string.exit_app_question))
            onNegative(ctx.getString(R.string.no))
            onPositive(ctx.getString(R.string.yes)) { exitProcess(0) }
        }
    }
}

