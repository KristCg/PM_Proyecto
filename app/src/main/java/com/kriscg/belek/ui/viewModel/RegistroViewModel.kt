package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegistroUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val registroSuccess: Boolean = false
)

class RegistroViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(username = username, error = null)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    fun register() {
        val state = _uiState.value

        if (state.username.isBlank()) {
            _uiState.value = state.copy(error = "Por favor ingresa un nombre de usuario")
            return
        }

        if (state.username.length < 3) {
            _uiState.value = state.copy(error = "El nombre de usuario debe tener al menos 3 caracteres")
            return
        }

        if (state.email.isBlank()) {
            _uiState.value = state.copy(error = "Por favor ingresa tu correo electrónico")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.value = state.copy(error = "Por favor ingresa un correo electrónico válido")
            return
        }

        if (state.password.isBlank()) {
            _uiState.value = state.copy(error = "Por favor ingresa una contraseña")
            return
        }

        if (state.password.length < 6) {
            _uiState.value = state.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            authRepository.signUp(state.email, state.password, state.username)
                .onSuccess {
                    _uiState.value = RegistroUiState(registroSuccess = true)
                }
                .onFailure { exception ->
                    _uiState.value = state.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al registrarse"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = RegistroUiState()
    }
}