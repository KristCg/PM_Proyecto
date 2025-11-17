package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.domain.Event
import com.kriscg.belek.domain.PlaceRecommendation
import com.kriscg.belek.data.repository.EventRepository
import com.kriscg.belek.data.repository.RecommendationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val eventRepository: EventRepository = EventRepository(),
    private val recommendationRepository: RecommendationRepository = RecommendationRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    fun loadEventDetails(eventId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            eventRepository.getEventById(eventId)
                .onSuccess { event ->
                    _uiState.update { it.copy(event = event) }
                    loadRecommendations(eventId)
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al cargar el evento"
                        )
                    }
                }
        }
    }

    private suspend fun loadRecommendations(eventId: String) {
        recommendationRepository.getRecommendationsGroupedByType(eventId)
            .onSuccess { grouped ->
                _uiState.update {
                    it.copy(
                        hotels = grouped["hotel"] ?: emptyList(),
                        restaurants = grouped["restaurant"] ?: emptyList(),
                        attractions = grouped["attraction"] ?: emptyList(),
                        general = grouped["general"] ?: emptyList(),
                        isLoading = false
                    )
                }
            }
            .onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
    }

    fun retry(eventId: String) {
        loadEventDetails(eventId)
    }
}

data class EventDetailUiState(
    val event: Event? = null,
    val hotels: List<PlaceRecommendation> = emptyList(),
    val restaurants: List<PlaceRecommendation> = emptyList(),
    val attractions: List<PlaceRecommendation> = emptyList(),
    val general: List<PlaceRecommendation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)