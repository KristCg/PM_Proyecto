package com.kriscg.belek.ui.navigation.yourTrip

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.yourTrip.TuViajeScreen

@Serializable
object YourTrip

fun NavGraphBuilder.yourTripNavigation(
    onNavigateBack: () -> Unit
) {
    composable<YourTrip> {
        TuViajeScreen(
            onBackClick = onNavigateBack
        )
    }
}
