package com.kriscg.belek.ui.navigation.config

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.profile.ConfigScreen

@Serializable
object Config

fun NavGraphBuilder.configNavigation(
    onNavigateBack: () -> Unit
) {
    composable<Config> {
        ConfigScreen(
            onBackClick = onNavigateBack
        )
    }
}