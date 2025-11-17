package com.kriscg.belek.ui.navigation.yourTrip

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.yourTrip.TuViajeScreen

@Serializable
data class YourTrip(
    val tipo: String? = null,
    val presupuesto: String? = null,
    val ambientes: String? = null,
    val departamento: String? = null
)

fun NavGraphBuilder.yourTripNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (Int) -> Unit
) {
    composable<YourTrip> { backStackEntry ->
        val args = backStackEntry.toRoute<YourTrip>()
        TuViajeScreen(
            tipo = args.tipo,
            presupuesto = args.presupuesto,
            ambientesJson = args.ambientes,
            onBackClick = onNavigateBack,
            onLugarClick = onNavigateToDetails
        )
    }
}