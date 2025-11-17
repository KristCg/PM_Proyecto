package com.kriscg.belek.ui.navigation.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.profile.ProfileScreen

@Serializable
object ProfileScreen

fun NavGraphBuilder.profileNavigation(
    onNavigateBack: () -> Unit,
    onToEditProfile: () -> Unit,
    onNavigateToDetails: (Int) -> Unit
) {
    composable<ProfileScreen> {
        ProfileScreen(
            onBackClick = onNavigateBack,
            onEditProfileClick = onToEditProfile,
            onLugarClick = onNavigateToDetails
        )
    }
}