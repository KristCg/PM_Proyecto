package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(username = username, error = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    fun login() {
        val state = _uiState.value

        if (state.username.isBlank()) {
            _uiState.value = state.copy(error = "Por favor ingresa tu usuario o email")
            return
        }

        if (state.password.isBlank()) {
            _uiState.value = state.copy(error = "Por favor ingresa tu contraseña")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            val email = state.username

            authRepository.signIn(email, state.password)
                .onSuccess {
                    _uiState.value = LoginUiState(loginSuccess = true)
                }
                .onFailure { exception ->
                    _uiState.value = state.copy(
                        isLoading = false,
                        error = when {
                            exception.message?.contains("Invalid login credentials") == true ->
                                "Usuario o contraseña incorrectos"
                            exception.message?.contains("Email not confirmed") == true ->
                                "Por favor confirma tu email"
                            else -> exception.message ?: "Error al iniciar sesión"
                        }
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}