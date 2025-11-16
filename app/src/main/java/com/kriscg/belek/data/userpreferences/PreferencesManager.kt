package com.kriscg.belek.data.userpreferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

data class UserPreferences(
    val isDarkTheme: Boolean = false,
    val language: String = "es",
    val languageDisplay: String = "Español",
    val currency: String = "Q",
    val isPublicInfoEnabled: Boolean = false
)

class PreferencesManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _preferencesFlow = MutableStateFlow(loadPreferences())
    val preferencesFlow: StateFlow<UserPreferences> = _preferencesFlow.asStateFlow()

    private fun loadPreferences(): UserPreferences {
        val languageCode = sharedPreferences.getString(KEY_LANGUAGE, "es") ?: "es"
        val currencyCode = sharedPreferences.getString(KEY_CURRENCY, "Q") ?: "Q"
        return UserPreferences(
            isDarkTheme = sharedPreferences.getBoolean(KEY_DARK_THEME, false),
            language = languageCode,
            languageDisplay = getLanguageDisplay(languageCode),
            currency = currencyCode,
            isPublicInfoEnabled = sharedPreferences.getBoolean(KEY_PUBLIC_INFO, false)
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
    }

    fun setCurrency(currencySymbol: String) {
        sharedPreferences.edit { putString(KEY_CURRENCY, currencySymbol) }
        _preferencesFlow.value = _preferencesFlow.value.copy(currency = currencySymbol)
    }

    fun setPublicInfo(isPublic: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_PUBLIC_INFO, isPublic) }
        _preferencesFlow.value = _preferencesFlow.value.copy(isPublicInfoEnabled = isPublic)
    }

    private fun getLanguageDisplay(code: String): String {
        return when (code) {
            "es" -> "Español"
            "en" -> "English"
            else -> "Español"
        }
    }

    fun getLanguageCode(displayName: String): String {
        return when (displayName) {
            "Español" -> "es"
            "English" -> "en"
            else -> "es"
        }
    }

    companion object {
        private const val PREFS_NAME = "belek_preferences"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_CURRENCY = "currency"
        private const val KEY_PUBLIC_INFO = "public_info"

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