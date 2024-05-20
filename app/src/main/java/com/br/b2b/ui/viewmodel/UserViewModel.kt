package com.br.b2b.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.b2b.data.local.datastore.DataStoreManager
import com.br.b2b.domain.model.User
import com.br.b2b.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    fun fetchAllUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                userRepository.getAllUsers()
            }.onSuccess { users ->
                _users.value = users
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.update(user)
        }
    }

    fun logout(onSignOut: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.logout()
            userRepository.deleteAll()
            withContext(Dispatchers.Main) {
                onSignOut.invoke()
            }
        }
    }

    fun insertUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.insertAll(user)
        }
    }

    fun deleteAllUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.deleteAll()
        }
    }
}