package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.data.models.Viaje
import com.kriscg.belek.data.repository.AuthRepository
import com.kriscg.belek.data.repository.ViajesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class EncuestaUiState(
    val destino: String = "",
    val tipoSeleccionado: String = "",
    val presupuestoSeleccionado: String = "",
    val interesesSeleccionados: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val viajeCreado: Boolean = false
)

class EncuestaViewModel(
    private val viajesRepository: ViajesRepository = ViajesRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EncuestaUiState())
    val uiState: StateFlow<EncuestaUiState> = _uiState.asStateFlow()

    fun onDestinoChange(destino: String) {
        _uiState.value = _uiState.value.copy(destino = destino)
    }

    fun onTipoSelected(tipo: String) {
        val currentTipo = _uiState.value.tipoSeleccionado
        _uiState.value = _uiState.value.copy(
            tipoSeleccionado = if (currentTipo == tipo) "" else tipo
        )
    }

    fun onPresupuestoSelected(presupuesto: String) {
        val currentPresupuesto = _uiState.value.presupuestoSeleccionado
        _uiState.value = _uiState.value.copy(
            presupuestoSeleccionado = if (currentPresupuesto == presupuesto) "" else presupuesto
        )
    }

    fun onInteresToggled(interes: String) {
        val currentIntereses = _uiState.value.interesesSeleccionados.toMutableSet()
        if (currentIntereses.contains(interes)) {
            currentIntereses.remove(interes)
        } else {
            currentIntereses.add(interes)
        }
        _uiState.value = _uiState.value.copy(interesesSeleccionados = currentIntereses)
    }

    fun guardarViaje() {
        val state = _uiState.value

        // Validaciones
        if (state.destino.isBlank()) {
            _uiState.value = state.copy(error = "Por favor ingresa un destino")
            return
        }

        if (state.tipoSeleccionado.isBlank()) {
            _uiState.value = state.copy(error = "Por favor selecciona un tipo de viaje")
            return
        }

        if (state.presupuestoSeleccionado.isBlank()) {
            _uiState.value = state.copy(error = "Por favor selecciona un presupuesto")
            return
        }

        val userId = authRepository.getCurrentUserId()
        if (userId == null) {
            _uiState.value = state.copy(error = "Debes iniciar sesión para crear un viaje")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)

            // Convertir intereses a JSON string
            val interesesJson = Json.encodeToString(state.interesesSeleccionados.toList())

            val viaje = Viaje(
                usuarioId = userId,
                destino = state.destino,
                tipo = state.tipoSeleccionado,
                presupuesto = state.presupuestoSeleccionado,
                intereses = interesesJson
            )

            viajesRepository.crearViaje(viaje)
                .onSuccess {
                    _uiState.value = state.copy(
                        isLoading = false,
                        viajeCreado = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = state.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al guardar el viaje"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = EncuestaUiState()
    }
}