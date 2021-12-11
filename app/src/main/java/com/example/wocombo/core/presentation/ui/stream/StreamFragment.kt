package com.example.wocombo.core.presentation.ui.stream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.databinding.FragmentAboutBinding
import com.example.wocombo.databinding.FragmentStreamBinding

class StreamFragment : Fragment() {


    private val binding by viewInflateBinding(FragmentStreamBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root
}