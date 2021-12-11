package com.example.wocombo.core.presentation.ui.transmissionlist.schedules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.databinding.FragmentScheduleListBinding

class ScheduleListFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentScheduleListBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root
}