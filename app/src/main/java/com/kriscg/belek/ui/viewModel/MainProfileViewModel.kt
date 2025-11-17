package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.domain.Usuario
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.domain.Resena
import com.kriscg.belek.data.repository.AuthRepository
import com.kriscg.belek.data.repository.UsuariosRepository
import com.kriscg.belek.data.repository.FavoritosRepository
import com.kriscg.belek.data.repository.LugaresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainProfileUiState(
    val usuario: Usuario? = null,
    val favoritos: List<Lugar> = emptyList(),
    val guardados: List<Lugar> = emptyList(),
    val resenas: List<Pair<Resena, Lugar?>> = emptyList(),
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MainProfileViewModel(
    private val usuariosRepository: UsuariosRepository = UsuariosRepository(),
    private val authRepository: AuthRepository = AuthRepository(),
    private val favoritosRepository: FavoritosRepository = FavoritosRepository(),
    private val lugaresRepository: LugaresRepository = LugaresRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainProfileUiState())
    val uiState: StateFlow<MainProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun refreshProfile() {
        loadUserProfile()
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
                    loadFavoritos(userId)
                    loadGuardados(userId)
                    loadResenas(userId)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar perfil"
                    )
                }
        }
    }

    private fun loadFavoritos(userId: String) {
        viewModelScope.launch {
            favoritosRepository.getFavoritosByUsuario(userId)
                .onSuccess { favoritos ->
                    val lugares = mutableListOf<Lugar>()
                    favoritos.forEach { favorito ->
                        lugaresRepository.getLugarById(favorito.lugarId)
                            .onSuccess { lugar ->
                                lugares.add(lugar)
                            }
                    }
                    _uiState.value = _uiState.value.copy(favoritos = lugares)
                }
                .onFailure {
                    println("Error al cargar favoritos: ${it.message}")
                }
        }
    }

    private fun loadGuardados(userId: String) {
        viewModelScope.launch {
            favoritosRepository.getGuardadosByUsuario(userId)
                .onSuccess { guardados ->
                    val lugares = mutableListOf<Lugar>()
                    guardados.forEach { guardado ->
                        lugaresRepository.getLugarById(guardado.lugarId)
                            .onSuccess { lugar ->
                                lugares.add(lugar)
                            }
                    }
                    _uiState.value = _uiState.value.copy(guardados = lugares)
                }
                .onFailure {
                    println("Error al cargar guardados: ${it.message}")
                }
        }
    }

    private fun loadResenas(userId: String) {
        viewModelScope.launch {
            // Obtener todas las reseñas del usuario
            lugaresRepository.getAllLugares()
                .onSuccess { lugares ->
                    val resenasConLugar = mutableListOf<Pair<Resena, Lugar?>>()

                    lugares.forEach { lugar ->
                        lugaresRepository.getResenasByLugarId(lugar.id ?: 0)
                            .onSuccess { resenas ->
                                resenas.filter { it.usuarioId == userId }.forEach { resena ->
                                    resenasConLugar.add(Pair(resena, lugar))
                                }
                            }
                    }

                    _uiState.value = _uiState.value.copy(resenas = resenasConLugar)
                }
                .onFailure {
                    println("Error al cargar reseñas: ${it.message}")
                }
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = index)
    }

}