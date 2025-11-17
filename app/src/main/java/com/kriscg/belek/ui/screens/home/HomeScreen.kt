package com.kriscg.belek.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.R
import androidx.compose.animation.AnimatedVisibility
import com.kriscg.belek.ui.theme.BelekTheme
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kriscg.belek.ui.viewModel.HomeViewModel
import com.kriscg.belek.ui.viewModel.LugarUI
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import coil.compose.AsyncImage
import com.kriscg.belek.rememberUserLocation
import com.kriscg.belek.ui.screens.details.GoogleMapView

enum class MenuOption {
    VIEW_PROFILE,
    ADD_ACCOUNT,
    HISTORY,
    SETTINGS,
    LOGOUT
}

@Composable
fun FloatingMenuButton(
    onNuevoViaje: () -> Unit = {},
    onCalendario: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedVisibility(visible = expanded) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MenuPill(
                        text = stringResource(R.string.nuevo_viaje),
                        icon = Icons.Default.Add
                    ) {
                        expanded = false
                        onNuevoViaje()
                    }

                    MenuPill(
                        text = stringResource(R.string.calendario),
                        icon = Icons.Default.DateRange
                    ) {
                        expanded = false
                        onCalendario()
                    }
                }
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                val icon = if (expanded) Icons.Default.Close else Icons.Default.Add
                Icon(icon, contentDescription = if (expanded) stringResource(R.string.cerrar) else stringResource(R.string.nuevo_viaje))
            }
        }
    }
}

@Composable
private fun MenuPill(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 6.dp,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CustomTabs(
    tabs: List<String> = listOf(stringResource(R.string.lista), stringResource(R.string.mapa)),
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(28.dp)
            )
            .clip(RoundedCornerShape(28.dp))
    ) {
        tabs.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (index == selectedTab)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.surface,
                        shape = when (index) {
                            0 -> RoundedCornerShape(topStart = 26.dp, bottomStart = 26.dp)
                            tabs.size - 1 -> RoundedCornerShape(topEnd = 26.dp, bottomEnd = 26.dp)
                            else -> RoundedCornerShape(0.dp)
                        }
                    )
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (index == selectedTab)
                        MaterialTheme.colorScheme.onSecondaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ListaContent(
    lugares: List<LugarUI>,
    onLugarClick: (Int) -> Unit = {}
) {
    if (lugares.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_lugares_filtros),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = lugares,
                key = { lugar -> lugar.id }
            ) { lugar ->
                LugarCard(
                    lugar = lugar,
                    onClick = { onLugarClick(lugar.id) }
                )
            }
        }
    }
}

@Composable
fun MapContent(
    lugares: List<LugarUI>,
    userLocation: LatLng?,
    onLugarClick: (Int) -> Unit = {}
) {
    val defaultLocation = LatLng(14.6349, -90.5069)

    val markers = remember(lugares) {
        lugares.mapNotNull { lugar ->
            val lat = lugar.latitud
            val lng = lugar.longitud
            if (lat != null && lng != null) {
                lugar to LatLng(lat, lng)
            } else {
                null
            }
        }
    }

    val startLocation = remember(userLocation, markers) {
        val inGuatemala = userLocation?.let { isInGuatemala(it) } ?: false
        when {
            userLocation != null && inGuatemala -> userLocation
            markers.isNotEmpty() -> markers.first().second
            else -> defaultLocation
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 8f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState
        ) {
            markers.forEach { (lugar, latLng) ->
                val markerState = rememberMarkerState(position = latLng)
                Marker(
                    state = markerState,
                    title = lugar.nombre,
                    snippet = lugar.descripcion,
                    onClick = {
                        onLugarClick(lugar.id)
                        false
                    }
                )
            }
        }
    }
}



@Composable
fun ProfileDrawerContent(
    onMenuItemClick: (MenuOption) -> Unit = {},
    onCloseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.BottomStart
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.perfil),
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.nombre_usuario),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar menú",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        val menuItems = listOf(
            Triple(stringResource(R.string.ver_perfil), Icons.Default.AccountCircle, MenuOption.VIEW_PROFILE),
            Triple(stringResource(R.string.agregar_cuenta), Icons.Default.Add, MenuOption.ADD_ACCOUNT),
            Triple(stringResource(R.string.historial), Icons.Default.Menu, MenuOption.HISTORY),
            Triple(stringResource(R.string.configuracion_privacidad), Icons.Default.Settings, MenuOption.SETTINGS),
            Triple(stringResource(R.string.cerrar_sesion), Icons.AutoMirrored.Filled.KeyboardArrowLeft, MenuOption.LOGOUT),
        )

        menuItems.forEachIndexed { index, (title, icon, option) ->
            if (index == 2 || index == 4) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMenuItemClick(option) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun isInGuatemala(location: LatLng): Boolean {
    val lat = location.latitude
    val lng = location.longitude

    val minLat = 13.5
    val maxLat = 18.0
    val minLng = -92.5
    val maxLng = -88.0

    return lat in minLat..maxLat && lng in minLng..maxLng
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNuevoViajeClick: () -> Unit = {},
    onLugarClick: (Int) -> Unit = {},
    onMenuItemClick: (MenuOption) -> Unit = {},
    onNavigateToCalendar: () -> Unit =  {},
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }
    val userLocation = rememberUserLocation()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val preferencesManager = remember {
        com.kriscg.belek.data.userpreferences.PreferencesManager.getInstance(context)
    }
    val preferences by preferencesManager.preferencesFlow.collectAsState()

    LaunchedEffect(preferences.language) {
        viewModel.updateLanguage(preferences.language)
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            println("Error: $error")
            viewModel.clearError()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                ProfileDrawerContent(
                    onMenuItemClick = { menuOption ->
                        onMenuItemClick(menuOption) },
                    onCloseClick = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.hogar),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        actions = {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .clickable {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = stringResource(R.string.perfil),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    )
                },
                floatingActionButton = {
                    if (uiState.selectedTab == 0) {
                        FloatingMenuButton(
                            onNuevoViaje = onNuevoViajeClick,
                            onCalendario = onNavigateToCalendar
                        )
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            viewModel.onSearchQueryChange(it)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.buscar),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.buscar),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )

                    Text(
                        text = stringResource(R.string.lugares_populares),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    CustomTabs(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = { viewModel.onTabSelected(it) }
                    )

                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        when (uiState.selectedTab) {
                            0 -> ListaContent(
                                lugares = uiState.lugares,
                                onLugarClick = onLugarClick
                            )
                            1 -> MapContent(
                                lugares = uiState.lugares,
                                userLocation = userLocation,
                                onLugarClick = onLugarClick
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun LugarCard(
    lugar: LugarUI,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(106.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (lugar.imageUrl != null) {
            AsyncImage(
                model = lugar.imageUrl,
                contentDescription = lugar.nombre,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = lugar.imageRes)
            )
        } else {
            Image(
                painter = painterResource(id = lugar.imageRes),
                contentDescription = lugar.nombre,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = lugar.nombre,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = lugar.descripcion,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    BelekTheme {
        HomeScreen()
    }
}