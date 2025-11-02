package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.data.models.Usuario
import com.kriscg.belek.data.repository.AuthRepository
import com.kriscg.belek.data.repository.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainProfileUiState(
    val usuario: Usuario? = null,
    val favoritos: List<String> = emptyList(),
    val guardados: List<String> = emptyList(),
    val resenas: List<String> = emptyList(),
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MainProfileViewModel(
    private val usuariosRepository: UsuariosRepository = UsuariosRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainProfileUiState())
    val uiState: StateFlow<MainProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
        loadUserData()
    }

    private fun loadUserProfile() {
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

    private fun loadUserData() {
        // Aquí cargarías los datos reales desde la base de datos
        // Por ahora uso datos de ejemplo
        _uiState.value = _uiState.value.copy(
            favoritos = listOf("Lugar 1", "Lugar 2", "Lugar 3"),
            guardados = listOf("Artículo 1", "Artículo 2"),
            resenas = listOf("Reseña A", "Reseña B", "Reseña C", "Reseña D")
        )
    }

    fun onTabSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = index)
    }

    fun addToFavoritos(lugar: String) {
        val currentFavoritos = _uiState.value.favoritos.toMutableList()
        if (!currentFavoritos.contains(lugar)) {
            currentFavoritos.add(lugar)
            _uiState.value = _uiState.value.copy(favoritos = currentFavoritos)
        }
    }

    fun removeFromFavoritos(lugar: String) {
        val currentFavoritos = _uiState.value.favoritos.toMutableList()
        currentFavoritos.remove(lugar)
        _uiState.value = _uiState.value.copy(favoritos = currentFavoritos)
    }

    fun addToGuardados(item: String) {
        val currentGuardados = _uiState.value.guardados.toMutableList()
        if (!currentGuardados.contains(item)) {
            currentGuardados.add(item)
            _uiState.value = _uiState.value.copy(guardados = currentGuardados)
        }
    }

    fun removeFromGuardados(item: String) {
        val currentGuardados = _uiState.value.guardados.toMutableList()
        currentGuardados.remove(item)
        _uiState.value = _uiState.value.copy(guardados = currentGuardados)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}