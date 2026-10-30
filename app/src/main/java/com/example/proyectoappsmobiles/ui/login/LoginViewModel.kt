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

    private val _emailInput = MutableStateFlow("estudiante@estudio")
    val emailInput: StateFlow<String> = _emailInput.asStateFlow()

    private val _passwordInput = MutableStateFlow("123456")
    val passwordInput: StateFlow<String> = _passwordInput.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllUsers().collect { userList ->
                _users.value = userList
            }
        }
    }

    fun onEmailChange(newEmail: String) {
        _emailInput.value = newEmail.lowercase().trim()
        _errorMessage.value = null
    }

    fun onPasswordChange(newPassword: String) {
        _passwordInput.value = newPassword
        _errorMessage.value = null
    }

    fun selectPresetEmail(presetEmail: String) {
        _emailInput.value = presetEmail
        _errorMessage.value = null
    }

    fun login(
        onUtp: () -> Unit,
        onDocente: () -> Unit,
        onEstudiante: (String) -> Unit
    ) {
        val currentEmail = _emailInput.value.trim().lowercase()
        val foundUser = _users.value.find { it.email.lowercase() == currentEmail }

        if (foundUser == null) {
            _errorMessage.value = "Credenciales no encontradas. Usa: jefatura@utp, profesor@profe o estudiante@estudio"
            return
        }

        _errorMessage.value = null
        when (foundUser.role) {
            UserRole.UTP -> onUtp()
            UserRole.DOCENTE -> onDocente()
            UserRole.ESTUDIANTE -> onEstudiante(foundUser.id)
        }
    }
}
