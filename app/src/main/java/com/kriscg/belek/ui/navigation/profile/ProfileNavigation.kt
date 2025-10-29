package com.kriscg.belek.ui.navigation.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import com.kriscg.belek.ui.screens.profile.EditProfileScreen

@Serializable
object EditProfile

fun NavGraphBuilder.profileNavigation(
    onNavigateBack: () -> Unit
) {
    composable<EditProfile> {
        EditProfileScreen(
            onBackClick = onNavigateBack
        )
    }
}