package com.kriscg.belek.ui.navigation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.home.HomeScreen
import com.kriscg.belek.ui.screens.home.MenuOption
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
            onMenuItemClick = { menuOption ->
                when (menuOption) {
                    MenuOption.VIEW_PROFILE -> onNavigateToProfile()
                    MenuOption.ADD_ACCOUNT -> {
                    }
                    MenuOption.HISTORY -> {
                    }
                    MenuOption.SETTINGS -> onToConfig()
                    MenuOption.LOGOUT -> {
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            authRepository.signOut()
                            onLogout()
                        }
                    }
                }
            }
        )
    }
}