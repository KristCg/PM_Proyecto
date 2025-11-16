package com.kriscg.belek.ui.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.data.repository.LugaresRepository
import com.kriscg.belek.util.TranslationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LugarUI(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val imageRes: Int,
    val imageUrl: String? = null
)

data class HomeUiState(
    val lugares: List<LugarUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedTab: Int = 0,
    val currentLanguage: String = "es"
)

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: LugaresRepository = LugaresRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    init {
        loadLugares()
    }

    fun updateLanguage(newLanguage: String) {
        if (_uiState.value.currentLanguage != newLanguage) {
            _uiState.value = _uiState.value.copy(currentLanguage = newLanguage)
            // Recargar lugares con el nuevo idioma
            if (_uiState.value.searchQuery.isBlank()) {
                loadLugares()
            } else {
                searchLugares(_uiState.value.searchQuery)
            }
        }
    }

    fun loadLugares() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val currentLanguage = TranslationHelper.getCurrentLanguage(context)

            repository.getAllLugares()
                .onSuccess { lugaresDB ->
                    val lugares = lugaresDB.map { lugar ->
                        LugarUI(
                            id = lugar.id ?: 0,
                            nombre = TranslationHelper.getLugarNombre(lugar, currentLanguage),
                            descripcion = TranslationHelper.getLugarDescripcion(lugar, currentLanguage),
                            imageRes = getImageResourceForPlace(lugar.nombre),
                            imageUrl = lugar.imagenUrl
                        )
                    }

                    _uiState.value = _uiState.value.copy(
                        lugares = lugares,
                        isLoading = false,
                        currentLanguage = currentLanguage
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

            val currentLanguage = TranslationHelper.getCurrentLanguage(context)

            repository.searchLugares(query)
                .onSuccess { lugaresDB ->
                    val lugares = lugaresDB.map { lugar ->
                        LugarUI(
                            id = lugar.id ?: 0,
                            nombre = TranslationHelper.getLugarNombre(lugar, currentLanguage),
                            descripcion = TranslationHelper.getLugarDescripcion(lugar, currentLanguage),
                            imageRes = getImageResourceForPlace(lugar.nombre),
                            imageUrl = lugar.imagenUrl
                        )
                    }

                    _uiState.value = _uiState.value.copy(
                        lugares = lugares,
                        isLoading = false,
                        currentLanguage = currentLanguage
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
            else -> com.kriscg.belek.R.drawable.tikal_prueba
        }
    }
}