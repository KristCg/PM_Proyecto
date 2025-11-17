package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriscg.belek.domain.Event
import com.kriscg.belek.data.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel(
    private val repository: EventRepository = EventRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    val eventsForSelectedDate: StateFlow<List<Event>> =
        combine(_uiState, _selectedDate) { state, date ->
            state.events.filter { it.getLocalDate() == date }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadCurrentMonthEvents()
    }

    private fun loadCurrentMonthEvents() {
        val now = YearMonth.now()
        loadEventsForMonth(now)
    }

    fun loadEventsForMonth(yearMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            _currentMonth.value = yearMonth

            repository.getEventsByMonth(yearMonth.year, yearMonth.monthValue)
                .onSuccess { events ->
                    _uiState.update {
                        it.copy(
                            events = events,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al cargar eventos"
                        )
                    }
                }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        if (yearMonth != _currentMonth.value) {
            loadEventsForMonth(yearMonth)
        }
    }

    fun retry() {
        loadCurrentMonthEvents()
    }

    fun hasEventsOnDate(date: LocalDate): Boolean {
        return _uiState.value.events.any { it.getLocalDate() == date }
    }
}

data class CalendarUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)