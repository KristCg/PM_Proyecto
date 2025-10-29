package com.kriscg.belek.ui.navigation.details

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.details.DetailsScreen

@Serializable
data class Details(val lugarId: Int)

fun NavGraphBuilder.detailsNavigation(
    onNavigateBack: () -> Unit
) {
    composable<Details> { backStackEntry ->
        val args = backStackEntry.toRoute<Details>()
        DetailsScreen(
            lugarId = args.lugarId,
            onBackClick = onNavigateBack
        )
    }
}