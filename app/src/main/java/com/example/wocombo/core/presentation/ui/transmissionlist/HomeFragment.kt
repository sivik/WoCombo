package com.example.wocombo.core.presentation.ui.transmissionlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wocombo.databinding.FragmentHomeBinding
import com.example.wocombo.common.extensions.viewBinding
import com.example.wocombo.common.extensions.viewInflateBinding

class HomeFragment : Fragment() {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val vb by viewInflateBinding(FragmentHomeBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = vb.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textView: TextView = binding.textHome
        super.onViewCreated(view, savedInstanceState)
    }
}
