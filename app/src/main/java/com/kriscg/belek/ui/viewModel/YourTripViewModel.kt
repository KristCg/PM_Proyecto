package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.data.repository.LugaresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class YourTripUiState(
    val lugaresRecomendados: List<Lugar> = emptyList(),
    val tiposSeleccionados: Set<String> = setOf("Naturales", "Históricos"),
    val ambientesSeleccionados: Set<String> = setOf("Románticos", "Familiares"),
    val serviciosSeleccionados: Set<String> = setOf("Estacionamiento", "Wifi", "Petfriendly"),
    val preciosSeleccionados: Set<String> = setOf("Gama Media", "Económico"),
    val momentosSeleccionados: Set<String> = setOf("Matutino", "Mediodía"),
    val isLoading: Boolean = false,
    val error: String? = null
)

class YourTripViewModel(
    private val repository: LugaresRepository = LugaresRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(YourTripUiState())
    val uiState: StateFlow<YourTripUiState> = _uiState.asStateFlow()

    init {
        loadRecommendations()
    }

    fun onTipoToggled(tipo: String) {
        val currentTipos = _uiState.value.tiposSeleccionados.toMutableSet()
        if (currentTipos.contains(tipo)) {
            currentTipos.remove(tipo)
        } else {
            currentTipos.add(tipo)
        }
        _uiState.value = _uiState.value.copy(tiposSeleccionados = currentTipos)
        loadRecommendations()
    }

    fun onAmbienteToggled(ambiente: String) {
        val currentAmbientes = _uiState.value.ambientesSeleccionados.toMutableSet()
        if (currentAmbientes.contains(ambiente)) {
            currentAmbientes.remove(ambiente)
        } else {
            currentAmbientes.add(ambiente)
        }
        _uiState.value = _uiState.value.copy(ambientesSeleccionados = currentAmbientes)
        loadRecommendations()
    }

    fun onServicioToggled(servicio: String) {
        val currentServicios = _uiState.value.serviciosSeleccionados.toMutableSet()
        if (currentServicios.contains(servicio)) {
            currentServicios.remove(servicio)
        } else {
            currentServicios.add(servicio)
        }
        _uiState.value = _uiState.value.copy(serviciosSeleccionados = currentServicios)
        loadRecommendations()
    }

    fun onPrecioToggled(precio: String) {
        val currentPrecios = _uiState.value.preciosSeleccionados.toMutableSet()
        if (currentPrecios.contains(precio)) {
            currentPrecios.remove(precio)
        } else {
            currentPrecios.add(precio)
        }
        _uiState.value = _uiState.value.copy(preciosSeleccionados = currentPrecios)
        loadRecommendations()
    }

    fun onMomentoToggled(momento: String) {
        val currentMomentos = _uiState.value.momentosSeleccionados.toMutableSet()
        if (currentMomentos.contains(momento)) {
            currentMomentos.remove(momento)
        } else {
            currentMomentos.add(momento)
        }
        _uiState.value = _uiState.value.copy(momentosSeleccionados = currentMomentos)
        loadRecommendations()
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getAllLugares()
                .onSuccess { lugares ->
                    val filtrados = lugares.filter { lugar ->
                        val matchTipo = _uiState.value.tiposSeleccionados.isEmpty() ||
                                _uiState.value.tiposSeleccionados.any {
                                    lugar.tipo.contains(it, ignoreCase = true)
                                }

                        val matchAmbiente = _uiState.value.ambientesSeleccionados.isEmpty() ||
                                _uiState.value.ambientesSeleccionados.any {
                                    lugar.ambiente?.contains(it, ignoreCase = true) == true
                                }

                        val matchServicios = _uiState.value.serviciosSeleccionados.isEmpty() ||
                                _uiState.value.serviciosSeleccionados.any {
                                    lugar.servicios?.contains(it, ignoreCase = true) == true
                                }

                        val matchPrecio = _uiState.value.preciosSeleccionados.isEmpty() ||
                                _uiState.value.preciosSeleccionados.any {
                                    lugar.precio?.contains(it, ignoreCase = true) == true
                                }

                        matchTipo && matchAmbiente && matchServicios && matchPrecio
                    }

                    _uiState.value = _uiState.value.copy(
                        lugaresRecomendados = filtrados.sortedByDescending { it.calificacion },
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar recomendaciones"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}