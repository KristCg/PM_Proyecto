package com.kriscg.belek.ui.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ConfigUiState(
    val darkThemeEnabled: Boolean = false,
    val selectedLanguage: String = "Español",
    val selectedCurrency: String = "Quetzales (Q)",
    val publicInfoEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true
)

class ConfigViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()

    fun toggleDarkTheme() {
        _uiState.value = _uiState.value.copy(
            darkThemeEnabled = !_uiState.value.darkThemeEnabled
        )
    }

    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
    }

    fun setCurrency(currency: String) {
        _uiState.value = _uiState.value.copy(selectedCurrency = currency)
    }

    fun togglePublicInfo() {
        _uiState.value = _uiState.value.copy(
            publicInfoEnabled = !_uiState.value.publicInfoEnabled
        )
    }

    fun toggleNotifications() {
        _uiState.value = _uiState.value.copy(
            notificationsEnabled = !_uiState.value.notificationsEnabled
        )
    }

    fun getAvailableLanguages(): List<String> {
        return listOf("Español", "English", "Français", "Deutsch")
    }

    fun getAvailableCurrencies(): List<String> {
        return listOf(
            "Quetzales (Q)",
            "Dólares (USD)",
            "Euros (EUR)",
            "Pesos Mexicanos (MXN)"
        )
    }
}