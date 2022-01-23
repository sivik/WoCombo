package com.example.wocombo.core.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wocombo.core.domain.usecases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginResult = MutableStateFlow(LoginUseCase.Result())
    val loginResult: StateFlow<LoginUseCase.Result> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            flow {
                val request = LoginUseCase.Request(
                    username = username,
                    password = password
                )
                emit(loginUseCase.execute(request))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = LoginUseCase.Result()
            ).collect {
                _loginResult.value = it
            }
        }
    }
}