package com.kriscg.belek.ui.navigation.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.login.LoginScreen
import com.kriscg.belek.ui.screens.login.RegistroScreen

@Serializable
object AuthGraph

@Serializable
object Login

@Serializable
object Registro

fun NavGraphBuilder.authNavigation(
    onNavigateToRegistro: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    navigation<AuthGraph>(startDestination = Login) {
        composable<Login> {
            LoginScreen(
                onRegistroClick = onNavigateToRegistro,
                onLoginClick = onNavigateToHome
            )
        }

        composable<Registro> {
            RegistroScreen(
                onRegistroClick = onNavigateToHome
            )
        }
    }
}