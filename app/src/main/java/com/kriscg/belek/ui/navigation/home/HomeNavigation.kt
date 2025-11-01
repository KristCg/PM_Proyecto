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
    onNavigateToProfile: () -> Unit,
    onToConfig: () -> Unit
) {
    composable<Home> {
        HomeScreen(
            onNuevoViajeClick = onNavigateToEncuesta,
            onLugarClick = onNavigateToDetails,
            onMenuItemClick = { menuItem ->
                when (menuItem) {
                    "Ver perfil" -> onNavigateToProfile()
                    "Configuración y privacidad" -> {}
                    "Cerrar Sesión" -> onToConfig()
                    "Historial" -> {}
                }
            }
        )
    }
}