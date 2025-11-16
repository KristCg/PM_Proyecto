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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kriscg.belek.ui.viewModel.HomeViewModel
import com.kriscg.belek.ui.viewModel.LugarUI
import coil.compose.AsyncImage

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
fun MapContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.destino),
            contentDescription = stringResource(R.string.mapa),
            contentScale = ContentScale.Fit,
            alpha = 0.3f,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}

@Composable
fun ProfileDrawerContent(
    onMenuItemClick: (String) -> Unit = {}
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
        }

        val menuItems = listOf(
            stringResource(R.string.ver_perfil) to Icons.Default.AccountCircle,
            stringResource(R.string.agregar_cuenta) to Icons.Default.Add,
            stringResource(R.string.historial) to Icons.Default.Menu,
            stringResource(R.string.configuracion_privacidad) to Icons.Default.Settings,
            stringResource(R.string.cerrar_sesion) to Icons.AutoMirrored.Filled.KeyboardArrowLeft,
        )

        menuItems.forEachIndexed { index, (title, icon) ->
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
                    .clickable { onMenuItemClick(title) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNuevoViajeClick: () -> Unit = {},
    onLugarClick: (Int) -> Unit = {},
    onMenuItemClick: (String) -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }

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
        drawerContent = {
            ModalDrawerSheet {
                ProfileDrawerContent(
                    onMenuItemClick = { item ->
                        onMenuItemClick(item)
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
                    FloatingMenuButton(
                        onNuevoViaje = onNuevoViajeClick,
                        onCalendario = {}
                    )
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
                            1 -> MapContent()
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