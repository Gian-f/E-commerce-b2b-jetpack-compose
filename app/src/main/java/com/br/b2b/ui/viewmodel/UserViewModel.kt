package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.domain.model.User
import com.br.b2b.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    fun fetchAllUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                repository.getAllUsers()
            }.onSuccess { users ->
                _users.value = users
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.update(user)
        }
    }

    fun insertUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertAll(user)
        }
    }

    fun deleteAllUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAll()
        }
    }
}