package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.domain.Usuario
import com.kriscg.belek.data.repository.AuthRepository
import com.kriscg.belek.data.repository.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val usuario: Usuario? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditingUsername: Boolean = false,
    val isEditingEmail: Boolean = false,
    val isEditingDescripcion: Boolean = false,
    val isEditingPassword: Boolean = false,
    val updateSuccess: Boolean = false
)

class ProfileViewModel(
    private val usuariosRepository: UsuariosRepository = UsuariosRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val userId = authRepository.getCurrentUserId()

        if (userId == null) {
            _uiState.value = _uiState.value.copy(
                error = "Usuario no autenticado"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            usuariosRepository.getUsuarioById(userId)
                .onSuccess { usuario ->
                    _uiState.value = _uiState.value.copy(
                        usuario = usuario,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar perfil"
                    )
                }
        }
    }

    fun startEditingUsername() {
        _uiState.value = _uiState.value.copy(isEditingUsername = true)
    }

    fun startEditingEmail() {
        _uiState.value = _uiState.value.copy(isEditingEmail = true)
    }

    fun startEditingDescripcion() {
        _uiState.value = _uiState.value.copy(isEditingDescripcion = true)
    }

    fun startEditingPassword() {
        _uiState.value = _uiState.value.copy(isEditingPassword = true)
    }

    fun cancelEditing() {
        _uiState.value = _uiState.value.copy(
            isEditingUsername = false,
            isEditingEmail = false,
            isEditingDescripcion = false,
            isEditingPassword = false
        )
    }

    fun updateUsername(newUsername: String) {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            usuariosRepository.updateUsername(userId, newUsername)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEditingUsername = false,
                        updateSuccess = true
                    )
                    loadProfile()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al actualizar nombre de usuario"
                    )
                }
        }
    }

    fun updateEmail(newEmail: String) {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            usuariosRepository.updateEmail(userId, newEmail)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEditingEmail = false,
                        updateSuccess = true
                    )
                    loadProfile()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al actualizar email"
                    )
                }
        }
    }

    fun updateDescripcion(newDescripcion: String) {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            usuariosRepository.updateDescripcion(userId, newDescripcion)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEditingDescripcion = false,
                        updateSuccess = true
                    )
                    loadProfile()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al actualizar descripción"
                    )
                }
        }
    }

    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            authRepository.updatePassword(newPassword)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEditingPassword = false,
                        updateSuccess = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al actualizar contraseña"
                    )
                }
        }
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(updateSuccess = false)
    }

}