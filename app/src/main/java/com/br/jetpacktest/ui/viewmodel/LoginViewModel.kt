package com.br.jetpacktest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.jetpacktest.data.remote.dto.request.LoginRequest
import com.br.jetpacktest.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun onLoginChange(login: String) {
        _username.value = login
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun authenticateUser(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                authRepository.authenticate(LoginRequest(username.value, password.value))
            }.onSuccess { loginResponse ->
                val response = loginResponse.getOrNull()
                if (response != null && response.status) {
                    clear()
                    onSuccess.invoke()
                } else {
                    val error =
                        loginResponse.exceptionOrNull()?.message
                            ?: "E-mail ou senha inválidos!"
                    _loginError.value = error
                    onFailure.invoke()
                }
            }.onFailure {
                onFailure.invoke()
            }
            _isLoading.value = false
        }
    }

    private fun clear() {
        viewModelScope.launch {
            _username.emit("")
            _password.emit("")
        }
    }
}