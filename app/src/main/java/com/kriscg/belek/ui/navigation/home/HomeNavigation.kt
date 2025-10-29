package com.kriscg.belek.ui.navigation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.home.HomeScreen

@Serializable
object Home

fun NavGraphBuilder.homeNavigation(
    onNavigateToEncuesta: () -> Unit,
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    composable<Home> {
        HomeScreen(
            onNuevoViajeClick = onNavigateToEncuesta,
            onProfileClick = onNavigateToProfile,
            onLugarClick = onNavigateToDetails
        )
    }
}
