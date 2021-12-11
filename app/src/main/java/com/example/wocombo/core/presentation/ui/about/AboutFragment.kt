package com.example.wocombo.core.presentation.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.wocombo.R
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.databinding.FragmentAboutBinding
import org.koin.android.ext.android.inject

class AboutFragment : Fragment() {

    private val binding by viewInflateBinding(FragmentAboutBinding::inflate)
    private val navigation: BaseNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigation.popBackStack()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


}