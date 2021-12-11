package com.example.wocombo.core.presentation.ui.transmissionlist.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.databinding.FragmentEventListBinding

class EventListFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentEventListBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root
}