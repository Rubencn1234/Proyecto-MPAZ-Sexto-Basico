package com.example.proyectoappsmobiles.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappsmobiles.data.local.entity.UserEntity
import com.example.proyectoappsmobiles.data.model.UserRole
import com.example.proyectoappsmobiles.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository) : ViewModel() {

    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users.asStateFlow()

    private val _selectedUser = MutableStateFlow<UserEntity?>(null)
    val selectedUser: StateFlow<UserEntity?> = _selectedUser.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllUsers().collect { userList ->
                _users.value = userList
                if (_selectedUser.value == null && userList.isNotEmpty()) {
                    _selectedUser.value = userList.firstOrNull { it.role == UserRole.ESTUDIANTE } ?: userList.first()
                }
            }
        }
    }

    fun selectUser(user: UserEntity) {
        _selectedUser.value = user
    }
}
