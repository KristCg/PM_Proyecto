package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.domain.Resena
import com.kriscg.belek.domain.Favorito
import com.kriscg.belek.domain.Guardado
import com.kriscg.belek.data.repository.AuthRepository
import com.kriscg.belek.data.repository.LugaresRepository
import com.kriscg.belek.data.repository.FavoritosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailsUiState(
    val lugar: Lugar? = null,
    val resenas: List<Resena> = emptyList(),
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showReviewDialog: Boolean = false,
    val isSubmittingReview: Boolean = false,
    val reviewSubmitted: Boolean = false,
    val isFavorito: Boolean = false,
    val isGuardado: Boolean = false
)

class DetailsViewModel(
    private val lugaresRepository: LugaresRepository = LugaresRepository(),
    private val authRepository: AuthRepository = AuthRepository(),
    private val favoritosRepository: FavoritosRepository = FavoritosRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun loadLugarDetails(lugarId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            lugaresRepository.getLugarById(lugarId)
                .onSuccess { lugar ->
                    _uiState.value = _uiState.value.copy(
                        lugar = lugar,
                        isLoading = false
                    )
                    loadResenas(lugarId)
                    checkFavoritoStatus(lugarId)
                    checkGuardadoStatus(lugarId)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar detalles del lugar"
                    )
                }
        }
    }

    private fun loadResenas(lugarId: Int) {
        viewModelScope.launch {
            lugaresRepository.getResenasByLugarId(lugarId)
                .onSuccess { resenas ->
                    _uiState.value = _uiState.value.copy(resenas = resenas)
                }
                .onFailure { exception ->
                    println("Error al cargar reseñas: ${exception.message}")
                }
        }
    }

    private fun checkFavoritoStatus(lugarId: Int) {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            favoritosRepository.isFavorito(userId, lugarId)
                .onSuccess { isFav ->
                    _uiState.value = _uiState.value.copy(isFavorito = isFav)
                }
                .onFailure {
                    println("Error al verificar favorito: ${it.message}")
                }
        }
    }

    private fun checkGuardadoStatus(lugarId: Int) {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            favoritosRepository.isGuardado(userId, lugarId)
                .onSuccess { isGuard ->
                    _uiState.value = _uiState.value.copy(isGuardado = isGuard)
                }
                .onFailure {
                    println("Error al verificar guardado: ${it.message}")
                }
        }
    }

    fun toggleFavorito() {
        val lugar = _uiState.value.lugar ?: return
        val userId = authRepository.getCurrentUserId() ?: return
        val isFavorito = _uiState.value.isFavorito

        viewModelScope.launch {
            if (isFavorito) {
                favoritosRepository.removeFavorito(userId, lugar.id ?: 0)
                    .onSuccess {
                        _uiState.value = _uiState.value.copy(isFavorito = false)
                    }
                    .onFailure {
                        println("Error al quitar favorito: ${it.message}")
                    }
            } else {
                val favorito = Favorito(
                    usuarioId = userId,
                    lugarId = lugar.id ?: 0
                )
                favoritosRepository.addFavorito(favorito)
                    .onSuccess {
                        _uiState.value = _uiState.value.copy(isFavorito = true)
                    }
                    .onFailure {
                        println("Error al agregar favorito: ${it.message}")
                    }
            }
        }
    }

    fun toggleGuardado() {
        val lugar = _uiState.value.lugar ?: return
        val userId = authRepository.getCurrentUserId() ?: return
        val isGuardado = _uiState.value.isGuardado

        viewModelScope.launch {
            if (isGuardado) {
                favoritosRepository.removeGuardado(userId, lugar.id ?: 0)
                    .onSuccess {
                        _uiState.value = _uiState.value.copy(isGuardado = false)
                    }
                    .onFailure {
                        println("Error al quitar guardado: ${it.message}")
                    }
            } else {
                val guardado = Guardado(
                    usuarioId = userId,
                    lugarId = lugar.id ?: 0
                )
                favoritosRepository.addGuardado(guardado)
                    .onSuccess {
                        _uiState.value = _uiState.value.copy(isGuardado = true)
                    }
                    .onFailure {
                        println("Error al agregar guardado: ${it.message}")
                    }
            }
        }
    }

    fun onTabSelected(tab: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun showReviewDialog() {
        _uiState.value = _uiState.value.copy(showReviewDialog = true)
    }

    fun hideReviewDialog() {
        _uiState.value = _uiState.value.copy(
            showReviewDialog = false,
            reviewSubmitted = false
        )
    }

    fun submitReview(rating: Int, comment: String) {
        val lugar = _uiState.value.lugar ?: return
        val userId = authRepository.getCurrentUserId()

        if (userId == null) {
            _uiState.value = _uiState.value.copy(
                error = "Debes iniciar sesión para dejar una reseña"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmittingReview = true)

            val resena = Resena(
                lugarId = lugar.id ?: 0,
                usuarioId = userId,
                calificacion = rating,
                comentario = comment
            )

            lugaresRepository.addResena(resena)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isSubmittingReview = false,
                        reviewSubmitted = true
                    )
                    loadResenas(lugar.id ?: 0)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSubmittingReview = false,
                        error = exception.message ?: "Error al enviar reseña"
                    )
                }
        }
    }
}