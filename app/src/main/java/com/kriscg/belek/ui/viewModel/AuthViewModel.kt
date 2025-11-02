package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        _authState.value = _authState.value.copy(
            isLoggedIn = repository.isUserLoggedIn()
        )
    }

    fun signUp(email: String, password: String, username: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)

            repository.signUp(email, password, username)
                .onSuccess {
                    _authState.value = AuthState(
                        isLoading = false,
                        isSuccess = true,
                        isLoggedIn = true
                    )
                }
                .onFailure { exception ->
                    _authState.value = AuthState(
                        isLoading = false,
                        error = exception.message ?: "Error al registrarse"
                    )
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)

            repository.signIn(email, password)
                .onSuccess {
                    _authState.value = AuthState(
                        isLoading = false,
                        isSuccess = true,
                        isLoggedIn = true
                    )
                }
                .onFailure { exception ->
                    _authState.value = AuthState(
                        isLoading = false,
                        error = exception.message ?: "Error al iniciar sesión"
                    )
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            _authState.value = AuthState(isLoggedIn = false)
        }
    }

    fun resetState() {
        _authState.value = AuthState(isLoggedIn = repository.isUserLoggedIn())
    }
}