package com.example.wocombo.core.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.wocombo.R
import com.example.wocombo.app.MainActivity
import com.example.wocombo.common.extensions.AppBarHelper
import com.example.wocombo.common.extensions.viewInflateBinding
import com.example.wocombo.common.functional.observe
import com.example.wocombo.common.navigation.BaseNavigation
import com.example.wocombo.core.domain.usecases.LoginUseCase
import com.example.wocombo.databinding.FragmentLoginBinding
import de.mateware.snacky.Snacky
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val vm: LoginViewModel by viewModel()
    private val binding by viewInflateBinding(FragmentLoginBinding::inflate)
    private val navigation by inject<BaseNavigation>()
    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        observe(vm.loginLiveData, ::handleLogin)
        actionBar = (activity as MainActivity).supportActionBar
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppBarHelper.setShowHideAnimationForActionBar(actionBar)
        actionBar?.hide()
        initListeners()
    }

    private fun initListeners() {

        binding.tilPassword.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.btLogin.isEnabled) {
                binding.pbLoading.visibility = View.VISIBLE
                login()
            }
            false
        }

        binding.btLogin.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            binding.btLogin.isEnabled = false
            login()
        }

        binding.tvResetPassword.setOnClickListener {
            Snacky.builder()
                .setActivity(requireActivity())
                .setText(R.string.info_forgot_password)
                .setDuration(Snacky.LENGTH_SHORT)
                .info()
                .show()
        }
    }

    private fun login(){
        vm.login(
            binding.tilLogin.editText?.text.toString(),
            binding.tilPassword.editText?.text.toString()
        )
    }


    private fun handleLogin(result: LoginUseCase.Result?) {
        result?.let { response ->
            response.success?.let {
                navigation.openTransmissionList()
            }

            response.failure?.let {
                binding.btLogin.isEnabled = true
                Snacky.builder()
                    .setActivity(requireActivity())
                    .setText(R.string.err_login_failure)
                    .setDuration(Snacky.LENGTH_SHORT)
                    .error()
                    .show()
            }
        }
    }
}