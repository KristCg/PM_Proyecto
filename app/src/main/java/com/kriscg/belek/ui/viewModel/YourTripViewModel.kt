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
    val tiposSeleccionados: Set<String> = emptySet(),
    val ambientesSeleccionados: Set<String> = emptySet(),
    val serviciosSeleccionados: Set<String> = emptySet(),
    val preciosSeleccionados: Set<String> = emptySet(),
    val momentosSeleccionados: Set<String> = emptySet(),
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

    fun initializeFromEncuesta(tipo: String?, presupuesto: String?, ambientes: Set<String>?) {
        val tiposSet = if (!tipo.isNullOrBlank()) setOf(tipo) else emptySet()

        val preciosSet = when (presupuesto) {
            "Bajo" -> setOf("Económico")
            "Mediano" -> setOf("Gama Media")
            "Alto" -> setOf("Lujo")
            else -> emptySet()
        }

        val ambientesSet = ambientes ?: emptySet()

        println("DEBUG ViewModel: Tipos=$tiposSet, Precios=$preciosSet, Ambientes=$ambientesSet")

        _uiState.value = _uiState.value.copy(
            tiposSeleccionados = tiposSet,
            preciosSeleccionados = preciosSet,
            ambientesSeleccionados = ambientesSet
        )

        println("DEBUG ViewModel: Estado actualizado - ${_uiState.value.tiposSeleccionados}, ${_uiState.value.preciosSeleccionados}, ${_uiState.value.ambientesSeleccionados}")

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
                    val state = _uiState.value

                    val noFilters = state.tiposSeleccionados.isEmpty() &&
                            state.ambientesSeleccionados.isEmpty() &&
                            state.serviciosSeleccionados.isEmpty() &&
                            state.preciosSeleccionados.isEmpty() &&
                            state.momentosSeleccionados.isEmpty()

                    if (noFilters) {
                        _uiState.value = _uiState.value.copy(
                            lugaresRecomendados = lugares.sortedByDescending { it.calificacion },
                            isLoading = false
                        )
                        return@onSuccess
                    }

                    val filtrados = lugares.filter { lugar ->
                        val matchTipo = state.tiposSeleccionados.isEmpty() ||
                                state.tiposSeleccionados.any {
                                    lugar.tipo.contains(it, ignoreCase = true)
                                }

                        val matchAmbiente = state.ambientesSeleccionados.isEmpty() ||
                                state.ambientesSeleccionados.any {
                                    lugar.ambiente?.contains(it, ignoreCase = true) == true
                                }

                        val matchServicios = state.serviciosSeleccionados.isEmpty() ||
                                state.serviciosSeleccionados.any {
                                    lugar.servicios?.contains(it, ignoreCase = true) == true
                                }

                        val matchPrecio = state.preciosSeleccionados.isEmpty() ||
                                state.preciosSeleccionados.any { precioSeleccionado ->
                                    lugar.rangoPrecio?.contains(precioSeleccionado, ignoreCase = true) == true
                                }

                        val matchMomento = state.momentosSeleccionados.isEmpty() ||
                                state.momentosSeleccionados.any { momentoSeleccionado ->
                                    lugar.momentoDia?.contains(momentoSeleccionado, ignoreCase = true) == true
                                }

                        matchTipo && matchAmbiente && matchServicios && matchPrecio && matchMomento
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