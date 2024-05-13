package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.data.remote.dto.request.CreateUserRequest
import com.br.b2b.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userViewModel: UserViewModel,
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _cpf = MutableStateFlow("")
    val cpf: StateFlow<String> = _cpf

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _termsCondition = MutableStateFlow(false)
    private val termsCondition: StateFlow<Boolean> = _termsCondition

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    suspend fun updateName(name: String) {
        _name.emit(name)
    }

    suspend fun clearForm() {
        updateName("")
        updateEmail("")
        updatePassword("")
        updateTermsCondition(false)
    }

    suspend fun updateEmail(email: String) {
        _email.emit(email)
    }

    suspend fun updateCPF(cpf: String) {
        _cpf.emit(cpf)
    }

    suspend fun updatePassword(password: String) {
        _password.emit(password)
    }

    suspend fun updateTermsCondition(accepted: Boolean = false) {
        _termsCondition.emit(accepted)
    }

    fun createUser(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        if (isFormValid(onFailure)) return
        viewModelScope.launch {
            _isLoading.value = true
            val newUser = CreateUserRequest(
                name = name.value,
                email = email.value,
                password = password.value,
                confirmPassword = password.value,
                cpf = cpf.value,
                termsConditions = termsCondition.value
            )
            runCatching {
                repository.createUser(newUser)
            }.onSuccess { response ->
                val result = response.getOrNull()
                if (result != null && result.status) {
                    onSuccess.invoke()
                } else {
                    val error =
                        response.getOrNull()?.message
                            ?: "Existem informações inválidas. \n Por favor, verifique o formulário e tente novamente!"
                    onFailure.invoke()
                    _errorMessage.value = error
                }
            }.onFailure {
                onFailure.invoke()
            }
            _isLoading.value = false
        }
    }

    private fun isFormValid(onFailure: () -> Unit): Boolean {
        if (name.value.isEmpty()) {
            _errorMessage.value = "Nome é obrigatório!"
            onFailure.invoke()
            return true
        }

        if (email.value.isEmpty() || !isEmailValid(email.value)) {
            _errorMessage.value =
                if (email.value.isEmpty()) "Email é obrigatório!" else "Email inválido!"
            onFailure.invoke()
            return true
        }
        if (password.value.isEmpty() || password.value.length < 6) {
            _errorMessage.value =
                if (password.value.isEmpty()) "Senha é obrigatória!" else "Senha deve ter pelo menos 6 caracteres!"
            onFailure.invoke()
            return true
        }

        if (!termsCondition.value) {
            _errorMessage.value = "termos e condições precisam ser aceitos!"
            onFailure.invoke()
            return true
        }
        return false
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }
}