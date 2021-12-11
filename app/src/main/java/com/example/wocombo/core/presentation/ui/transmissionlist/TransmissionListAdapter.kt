package com.example.wocombo.core.presentation.ui.transmissionlist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wocombo.core.presentation.ui.transmissionlist.events.EventListFragment
import com.example.wocombo.core.presentation.ui.transmissionlist.schedules.ScheduleListFragment

class TransmissionListAdapter(transmissionListFragment: TransmissionListFragment) :
    FragmentStateAdapter(transmissionListFragment) {


    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EventListFragment()
            1 -> ScheduleListFragment()
            else -> EventListFragment()
        }
    }
}