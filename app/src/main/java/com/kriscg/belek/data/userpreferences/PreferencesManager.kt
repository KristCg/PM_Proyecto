package com.kriscg.belek.data.userpreferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import androidx.core.content.edit

data class UserPreferences(
    val isDarkTheme: Boolean = false,
    val language: String = "es",
    val languageDisplay: String = "Español",
    val currency: String = "Quetzales (Q)"
)

class PreferencesManager private constructor(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _preferencesFlow = MutableStateFlow(loadPreferences())
    val preferencesFlow: StateFlow<UserPreferences> = _preferencesFlow.asStateFlow()

    init {
        applyLanguage(_preferencesFlow.value.language)
    }

    private fun loadPreferences(): UserPreferences {
        val languageCode = sharedPreferences.getString(KEY_LANGUAGE, "es") ?: "es"
        return UserPreferences(
            isDarkTheme = sharedPreferences.getBoolean(KEY_DARK_THEME, false),
            language = languageCode,
            languageDisplay = getLanguageDisplay(languageCode),
            currency = sharedPreferences.getString(KEY_CURRENCY, "Quetzales (Q)") ?: "Quetzales (Q)"
        )
    }

    fun setDarkTheme(isDark: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_DARK_THEME, isDark) }
        _preferencesFlow.value = _preferencesFlow.value.copy(isDarkTheme = isDark)
    }

    fun setLanguage(languageCode: String, displayName: String) {
        sharedPreferences.edit { putString(KEY_LANGUAGE, languageCode) }
        _preferencesFlow.value = _preferencesFlow.value.copy(
            language = languageCode,
            languageDisplay = displayName
        )
        applyLanguage(languageCode)
    }

    fun setCurrency(currency: String) {
        sharedPreferences.edit { putString(KEY_CURRENCY, currency) }
        _preferencesFlow.value = _preferencesFlow.value.copy(currency = currency)
    }

    private fun applyLanguage(languageCode: String) {
        val locale = Locale.Builder().setLanguage(languageCode).build()

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.createConfigurationContext(config)
    }

    private fun getLanguageDisplay(code: String): String {
        return when (code) {
            "es" -> "Español"
            "en" -> "English"
            "fr" -> "Français"
            "de" -> "Deutsch"
            else -> "Español"
        }
    }

    fun getLanguageCode(displayName: String): String {
        return when (displayName) {
            "Español" -> "es"
            "English" -> "en"
            "Français" -> "fr"
            "Deutsch" -> "de"
            else -> "es"
        }
    }

    companion object {
        private const val PREFS_NAME = "belek_preferences"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_CURRENCY = "currency"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: PreferencesManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}