package com.kriscg.belek

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kriscg.belek.data.repository.AuthRepository
import com.kriscg.belek.ui.navigation.calendar.Calendar
import com.kriscg.belek.ui.navigation.calendar.EventDetail
import com.kriscg.belek.ui.navigation.calendar.calendarNavigation
import com.kriscg.belek.ui.theme.BelekTheme
import com.kriscg.belek.ui.navigation.login.AuthGraph
import com.kriscg.belek.ui.navigation.login.Registro
import com.kriscg.belek.ui.navigation.home.Home
import com.kriscg.belek.ui.navigation.encuestas.Encuesta
import com.kriscg.belek.ui.navigation.yourTrip.YourTrip
import com.kriscg.belek.ui.navigation.details.Details
import com.kriscg.belek.ui.navigation.profile.ProfileScreen
import com.kriscg.belek.ui.navigation.config.Config
import com.kriscg.belek.ui.navigation.profile.EditProfile
import com.kriscg.belek.ui.navigation.login.authNavigation
import com.kriscg.belek.ui.navigation.home.homeNavigation
import com.kriscg.belek.ui.navigation.encuestas.encuestaNavigation
import com.kriscg.belek.ui.navigation.yourTrip.yourTripNavigation
import com.kriscg.belek.ui.navigation.details.detailsNavigation
import com.kriscg.belek.ui.navigation.profile.profileNavigation
import com.kriscg.belek.ui.navigation.config.configNavigation
import com.kriscg.belek.ui.navigation.login.Login
import com.kriscg.belek.ui.navigation.profile.editProfileNavigation
import com.kriscg.belek.ui.navigation.calendar.calendarNavigation
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("belek_preferences", MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language", "es") ?: "es"

        val context = updateLocale(newBase, languageCode)
        super.attachBaseContext(context)
    }

    private fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale.Builder().setLanguage(languageCode).build()
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        com.kriscg.belek.data.SupabaseConfig.initialize(applicationContext)

        enableEdgeToEdge()
        setContent {
            val preferencesManager = remember {
                com.kriscg.belek.data.userpreferences.PreferencesManager.getInstance(applicationContext)
            }
            val preferences by preferencesManager.preferencesFlow.collectAsState()

            LaunchedEffect(preferences.language) {
                val currentLocale = resources.configuration.locales[0].language
                if (currentLocale != preferences.language) {
                    recreate()
                }
            }

            BelekTheme(darkTheme = preferences.isDarkTheme) {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authRepository = remember { AuthRepository() }

    var startDestination by remember {
        mutableStateOf<Any>(AuthGraph)
    }
    var isCheckingAuth by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)

        val isLoggedIn = authRepository.isUserLoggedIn()
        println("DEBUG Auth: Usuario logueado = $isLoggedIn")

        startDestination = if (isLoggedIn) {
            println("DEBUG Auth: Navegando a Home")
            Home
        } else {
            println("DEBUG Auth: Navegando a Login")
            AuthGraph
        }
        isCheckingAuth = false
    }

    if (isCheckingAuth) {
        androidx.compose.foundation.layout.Box(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authNavigation(
            onNavigateToRegistro = {
                navController.navigate(Registro)
            },
            onNavigateToHome = {
                navController.navigate(Home) {
                    popUpTo(AuthGraph) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )

        homeNavigation(
            onNavigateToEncuesta = {
                navController.navigate(Encuesta) {
                    launchSingleTop = true
                }
            },
            onNavigateToDetails = { lugarId ->
                navController.navigate(Details(lugarId = lugarId)) {
                    launchSingleTop = true
                }
            },
            onNavigateToProfile = {
                navController.navigate(ProfileScreen) {
                    launchSingleTop = true
                }
            },
            onToConfig = {
                navController.navigate(Config) {
                    launchSingleTop = true
                }
            },
            onLogout = {
                navController.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onNavigateToCalendar = {
                navController.navigate(Calendar) {
                    launchSingleTop = true
                }
            }

        )

        encuestaNavigation(
            onNavigateToYourTrip = { tipo, presupuesto, ambientes, departamento ->
                navController.navigate(
                    YourTrip(
                        tipo = tipo,
                        presupuesto = presupuesto,
                        ambientes = ambientes,
                        departamento = departamento
                    )
                ) {
                    launchSingleTop = true
                }
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )

        yourTripNavigation(
            onNavigateBack = {
                navController.navigate(Home) {
                    popUpTo(Home) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onNavigateToDetails = { lugarId ->
                navController.navigate(Details(lugarId = lugarId)) {
                    launchSingleTop = true
                }
            }
        )

        detailsNavigation(
            onNavigateBack = {
                navController.navigateUp()
            }
        )

        profileNavigation(
            onNavigateBack = {
                navController.navigateUp()
            },
            onToEditProfile = {
                navController.navigate(EditProfile) {
                    launchSingleTop = true
                }
            },
            onNavigateToDetails = { lugarId ->
                navController.navigate(Details(lugarId = lugarId)) {
                    launchSingleTop = true
                }
            }
        )

        configNavigation(
            onNavigateBack = {
                navController.navigateUp()
            }
        )

        editProfileNavigation(
            onNavigateBack = {
                navController.navigateUp()
            }
        )
        calendarNavigation(
            onNavigateToEventDetail = { eventId ->
                navController.navigate(EventDetail(eventId = eventId)) {
                    launchSingleTop = true
                }
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
}