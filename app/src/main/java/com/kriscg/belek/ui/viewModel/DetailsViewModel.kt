package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.domain.Resena
import com.kriscg.belek.data.repository.AuthRepository
import com.kriscg.belek.data.repository.LugaresRepository
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
    val reviewSubmitted: Boolean = false
)

class DetailsViewModel(
    private val lugaresRepository: LugaresRepository = LugaresRepository(),
    private val authRepository: AuthRepository = AuthRepository()
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}