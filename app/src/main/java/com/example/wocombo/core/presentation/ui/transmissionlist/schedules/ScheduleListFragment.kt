package com.example.wocombo.core.presentation.ui.transmissionlist.schedules

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wocombo.R

class ScheduleListFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleListFragment()
    }

    private lateinit var viewModel: ScheduleListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScheduleListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}