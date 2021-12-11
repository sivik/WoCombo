package com.example.wocombo.core.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wocombo.common.functional.SingleLiveData
import com.example.wocombo.core.domain.usecases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginLiveData: SingleLiveData<LoginUseCase.Result> by lazy { SingleLiveData() }
    val loginLiveData: LiveData<LoginUseCase.Result>
        get() = _loginLiveData

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val request = LoginUseCase.Request(
                username = username,
                password = password
            )
            val result = loginUseCase.execute(request)
            _loginLiveData.postValue(result)
        }
    }
}