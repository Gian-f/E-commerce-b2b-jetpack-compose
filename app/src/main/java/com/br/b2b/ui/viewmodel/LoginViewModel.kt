package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.data.remote.dto.request.LoginRequest
import com.br.b2b.domain.repository.AuthRepository
import com.br.b2b.domain.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoginLoading = MutableStateFlow(false)
    val isLoginLoading: StateFlow<Boolean> = _isLoginLoading

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
        _isLoginLoading.value = true
        viewModelScope.launch {
            runCatching {
                authRepository.authenticate(LoginRequest(username.value, password.value))
            }.onSuccess { loginResponse ->
                val response = loginResponse.getOrNull()
                if (response != null && response.status) {
                    clear()
                    onSuccess.invoke()
                } else {
                    _loginError.value = response?.message ?: "E-mail ou senha inválidos!"
                    onFailure.invoke()
                }
            }.onFailure {
                onFailure.invoke()
            }
            _isLoginLoading.value = false
        }
    }

    private fun clear() {
        viewModelScope.launch {
            _username.emit("")
            _password.emit("")
        }
    }
}