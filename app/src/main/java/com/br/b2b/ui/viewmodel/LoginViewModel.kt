package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.data.local.datastore.DataStoreManager
import com.br.b2b.data.remote.dto.request.LoginRequest
import com.br.b2b.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoginLoading = MutableStateFlow(false)
    val isLoginLoading: StateFlow<Boolean> = _isLoginLoading

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    init {
        getToken()
    }

    fun onLoginChange(login: String) {
        _username.value = login
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            dataStoreManager.saveToken(token)
        }
    }

    private fun getToken() {
        viewModelScope.launch {
            _token.value = dataStoreManager.getToken()
        }
    }

    fun authenticateUser(
        onSuccess: (token: String) -> Unit,
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
                    onSuccess.invoke(response.result)
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