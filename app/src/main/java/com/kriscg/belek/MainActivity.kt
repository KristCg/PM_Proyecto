package com.kriscg.belek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kriscg.belek.ui.theme.BelekTheme
import com.kriscg.belek.ui.navigation.login.AuthGraph
import com.kriscg.belek.ui.navigation.login.Registro
import com.kriscg.belek.ui.navigation.home.Home
import com.kriscg.belek.ui.navigation.encuestas.Encuesta
import com.kriscg.belek.ui.navigation.yourTrip.YourTrip
import com.kriscg.belek.ui.navigation.details.Details
import com.kriscg.belek.ui.navigation.profile.ProfileScreen
import com.kriscg.belek.ui.navigation.config.Config
import com.kriscg.belek.ui.screens.profile.EditProfileScreen
import com.kriscg.belek.ui.navigation.login.authNavigation
import com.kriscg.belek.ui.navigation.home.homeNavigation
import com.kriscg.belek.ui.navigation.encuestas.encuestaNavigation
import com.kriscg.belek.ui.navigation.yourTrip.yourTripNavigation
import com.kriscg.belek.ui.navigation.details.detailsNavigation
import com.kriscg.belek.ui.navigation.profile.profileNavigation
import com.kriscg.belek.ui.navigation.config.configNavigation
import com.kriscg.belek.ui.navigation.profile.editProfileNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BelekTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthGraph
    ) {
        authNavigation(
            onNavigateToRegistro = {
                navController.navigate(Registro)
            },
            onNavigateToHome = {
                navController.navigate(Home) {
                    popUpTo<AuthGraph> { inclusive = true }
                }
            }
        )

        homeNavigation(
            onNavigateToEncuesta = {
                navController.navigate(Encuesta)
            },
            onNavigateToDetails = { lugarId ->
                navController.navigate(Details(lugarId = lugarId))
            },
            onToProfile = {
                navController.navigate(ProfileScreen)
            },
            onToConfig = {
                navController.navigate(Config)
            }
        )

        encuestaNavigation(
            onNavigateToYourTrip = {
                navController.navigate(YourTrip)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        yourTripNavigation(
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        detailsNavigation(
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        profileNavigation(
            onNavigateBack = {
                navController.popBackStack()
            },
            onToEditProfile = {
                navController.navigate(EditProfile)
            }
        )

        configNavigation(
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        editProfileNavigation(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}