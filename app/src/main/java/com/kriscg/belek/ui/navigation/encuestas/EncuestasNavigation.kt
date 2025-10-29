package com.kriscg.belek.ui.navigation.encuestas

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.encuestas.EncuestaScreen

@Serializable
object Encuesta

fun NavGraphBuilder.encuestaNavigation(
    onNavigateToYourTrip: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<Encuesta> {
        EncuestaScreen(
            onVerOpcionesClick = onNavigateToYourTrip,
            onBackClick = onNavigateBack
        )
    }
}
