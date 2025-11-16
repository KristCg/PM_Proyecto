package com.kriscg.belek.ui.navigation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.home.HomeScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kriscg.belek.ui.viewModel.HomeViewModel
import com.kriscg.belek.data.repository.AuthRepository
import kotlinx.coroutines.launch

@Serializable
object Home

fun NavGraphBuilder.homeNavigation(
    onNavigateToEncuesta: () -> Unit,
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToProfile: () -> Unit,
    onToConfig: () -> Unit,
    onLogout: () -> Unit
) {
    composable<Home> {
        val authRepository = AuthRepository()

        HomeScreen(
            onNuevoViajeClick = onNavigateToEncuesta,
            onLugarClick = onNavigateToDetails,
            onMenuItemClick = { menuItem ->
                when (menuItem) {
                    "Ver perfil" -> onNavigateToProfile()
                    "Configuración y privacidad" -> onToConfig()
                    "Cerrar Sesión" -> {
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            authRepository.signOut()
                            onLogout()
                        }
                    }
                    "Historial" -> {}
                }
            }
        )
    }
}