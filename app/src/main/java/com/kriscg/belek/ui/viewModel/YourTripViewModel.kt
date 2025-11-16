package com.kriscg.belek.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.data.repository.LugaresRepository
import com.kriscg.belek.util.TranslationHelper
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
    application: Application
) : AndroidViewModel(application) {

    private val repository: LugaresRepository = LugaresRepository()

    private val _uiState = MutableStateFlow(YourTripUiState())
    val uiState: StateFlow<YourTripUiState> = _uiState.asStateFlow()

    init {
        loadRecommendations()
    }

    fun initializeFromEncuesta(tipo: String?, presupuesto: String?, ambientes: Set<String>?) {
        val tiposSet = if (!tipo.isNullOrBlank()) {
            val normalizedTipo = TranslationHelper.normalizeToSpanish(tipo)
            setOf(normalizedTipo)
        } else emptySet()

        val preciosSet = when {
            presupuesto != null -> {
                val normalizedPresupuesto = TranslationHelper.normalizeToSpanish(presupuesto)
                when (normalizedPresupuesto) {
                    "Bajo" -> setOf("Económico")
                    "Mediano" -> setOf("Gama Media")
                    "Alto" -> setOf("Lujo")
                    else -> emptySet()
                }
            }
            else -> emptySet()
        }

        val ambientesSet = ambientes?.map { TranslationHelper.normalizeToSpanish(it) }?.toSet() ?: emptySet()

        println("DEBUG ViewModel: Tipos=$tiposSet, Precios=$preciosSet, Ambientes=$ambientesSet")

        _uiState.value = _uiState.value.copy(
            tiposSeleccionados = tiposSet,
            preciosSeleccionados = preciosSet,
            ambientesSeleccionados = ambientesSet
        )

        loadRecommendations()
    }

    fun onTipoToggled(tipo: String) {
        val normalizedTipo = TranslationHelper.normalizeToSpanish(tipo)
        val currentTipos = _uiState.value.tiposSeleccionados.toMutableSet()

        if (currentTipos.contains(normalizedTipo)) {
            currentTipos.remove(normalizedTipo)
        } else {
            currentTipos.add(normalizedTipo)
        }
        _uiState.value = _uiState.value.copy(tiposSeleccionados = currentTipos)
        loadRecommendations()
    }

    fun onAmbienteToggled(ambiente: String) {
        val normalizedAmbiente = TranslationHelper.normalizeToSpanish(ambiente)
        val currentAmbientes = _uiState.value.ambientesSeleccionados.toMutableSet()

        if (currentAmbientes.contains(normalizedAmbiente)) {
            currentAmbientes.remove(normalizedAmbiente)
        } else {
            currentAmbientes.add(normalizedAmbiente)
        }
        _uiState.value = _uiState.value.copy(ambientesSeleccionados = currentAmbientes)
        loadRecommendations()
    }

    fun onServicioToggled(servicio: String) {
        val normalizedServicio = TranslationHelper.normalizeToSpanish(servicio)
        val currentServicios = _uiState.value.serviciosSeleccionados.toMutableSet()

        if (currentServicios.contains(normalizedServicio)) {
            currentServicios.remove(normalizedServicio)
        } else {
            currentServicios.add(normalizedServicio)
        }
        _uiState.value = _uiState.value.copy(serviciosSeleccionados = currentServicios)
        loadRecommendations()
    }

    fun onPrecioToggled(precio: String) {
        val normalizedPrecio = TranslationHelper.normalizeToSpanish(precio)
        val currentPrecios = _uiState.value.preciosSeleccionados.toMutableSet()

        if (currentPrecios.contains(normalizedPrecio)) {
            currentPrecios.remove(normalizedPrecio)
        } else {
            currentPrecios.add(normalizedPrecio)
        }
        _uiState.value = _uiState.value.copy(preciosSeleccionados = currentPrecios)
        loadRecommendations()
    }

    fun onMomentoToggled(momento: String) {
        val normalizedMomento = TranslationHelper.normalizeToSpanish(momento)
        val currentMomentos = _uiState.value.momentosSeleccionados.toMutableSet()

        if (currentMomentos.contains(normalizedMomento)) {
            currentMomentos.remove(normalizedMomento)
        } else {
            currentMomentos.add(normalizedMomento)
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