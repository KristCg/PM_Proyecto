package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.data.repository.LugaresRepository
import com.kriscg.belek.ui.screens.home.Lugar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val lugares: List<Lugar> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedTab: Int = 0
)

class HomeViewModel(
    private val repository: LugaresRepository = LugaresRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadLugares()
    }

    fun loadLugares() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getAllLugares()
                .onSuccess { lugaresDB ->
                    val lugares = lugaresDB.map { lugar ->
                        Lugar(
                            id = lugar.id ?: 0,
                            nombre = lugar.nombre,
                            descripcion = lugar.descripcion,
                            imageRes = getImageResourceForPlace(lugar.nombre)
                        )
                    }

                    _uiState.value = _uiState.value.copy(
                        lugares = lugares,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar lugares"
                    )
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        if (query.isBlank()) {
            loadLugares()
        } else {
            searchLugares(query)
        }
    }

    private fun searchLugares(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.searchLugares(query)
                .onSuccess { lugaresDB ->
                    val lugares = lugaresDB.map { lugar ->
                        Lugar(
                            id = lugar.id ?: 0,
                            nombre = lugar.nombre,
                            descripcion = lugar.descripcion,
                            imageRes = getImageResourceForPlace(lugar.nombre)
                        )
                    }

                    _uiState.value = _uiState.value.copy(
                        lugares = lugares,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error en la búsqueda"
                    )
                }
        }
    }

    fun onTabSelected(tab: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun getImageResourceForPlace(nombre: String): Int {
        return when {
            nombre.contains("Tikal", ignoreCase = true) -> com.kriscg.belek.R.drawable.tikal
            nombre.contains("Antigua", ignoreCase = true) -> com.kriscg.belek.R.drawable.antigua
            nombre.contains("Atitlán", ignoreCase = true) -> com.kriscg.belek.R.drawable.atitlan
            nombre.contains("Semuc", ignoreCase = true) -> com.kriscg.belek.R.drawable.semuc
            else -> com.kriscg.belek.R.drawable.tikal // default
        }
    }
}