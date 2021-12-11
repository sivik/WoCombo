package com.example.wocombo.core.presentation.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wocombo.R
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentAboutBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


}