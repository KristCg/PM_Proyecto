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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import com.kriscg.belek.ui.theme.BelekTheme
import kotlinx.coroutines.launch

data class Lugar(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val imageRes: Int
)

@Composable
fun FloatingMenuButton(
    onNuevoViaje: () -> Unit = {},
    onCalendario: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    val fabColor = Color(0xFFD32F2F)
    val pillBg = Color(0xFFFFE4E6)
    val pillBorder = Color(0xFFF3B4B8)
    val pillContent = Color(0xFF8A1C24)

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
                        text = "Nuevo Viaje",
                        pillBg = pillBg,
                        pillBorder = pillBorder,
                        contentColor = pillContent,
                        icon = Icons.Default.Add
                    ) {
                        expanded = false
                        onNuevoViaje()
                    }

                    MenuPill(
                        text = "Calendario",
                        pillBg = pillBg,
                        pillBorder = pillBorder,
                        contentColor = pillContent,
                        icon = Icons.Default.DateRange
                    ) {
                        expanded = false
                        onCalendario()
                    }
                }
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = fabColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                val icon = if (expanded) Icons.Default.Close else Icons.Default.Add
                Icon(icon, contentDescription = if (expanded) "Cerrar" else "Abrir menú")
            }
        }
    }
}

@Composable
private fun MenuPill(
    text: String,
    pillBg: Color,
    pillBorder: Color,
    contentColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        color = pillBg,
        contentColor = contentColor,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 6.dp,
        modifier = Modifier
            .border(1.dp, pillBorder, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(10.dp))
            Text(text, fontWeight = FontWeight.SemiBold)
        }
    }
}


@Composable
fun CustomTabs(
    tabs: List<String> = listOf("Lista", "Mapa"),
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
                            MaterialTheme.colorScheme.background,
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
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun ListaContent(
    lugares: List<Lugar>,
    onLugarClick: (Int) -> Unit = {}
) {
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

@Composable
fun MapContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFECECEC)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.destino),
            contentDescription = "Destino",
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
                    contentDescription = "Usuario",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nombre de Usuario",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        val menuItems = listOf(
            "Ver perfil" to Icons.Default.AccountCircle,
            "Agregar otra cuenta" to Icons.Default.Add,
            "Historial" to Icons.Default.Menu,
            "Configuración y privacidad" to Icons.Default.Settings,
            "Cerrar Sesión" to Icons.Default.ExitToApp,
        )

        menuItems.forEachIndexed { index, (title, icon) ->
            if (index == 2 || index == 4) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.5.dp
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
    onToProfile: () -> Unit = {},
    onLugarClick: (Int) -> Unit = {},
    onMenuItemClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }

    val lugares = listOf(
        Lugar(1, "Tikal, Guatemala", "Antigua capital maya entre la selva.", R.drawable.tikal),
        Lugar(2, "Antigua Guatemala", "Ciudad colonial rodeada de volcanes.", R.drawable.antigua),
        Lugar(3, "Lago de Atitlán", "El lago más bello del mundo con pueblos llenos de color.", R.drawable.atitlan),
        Lugar(4, "Semuc Champey", "Piscinas naturales de aguas turquesas en medio de la jungla.", R.drawable.semuc)
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                                text = "HOGAR",
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
                                    contentDescription = "Perfil",
                                    tint = MaterialTheme.colorScheme.primary
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
                        onValueChange = { query = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Buscar",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFE6F4F1),
                            unfocusedContainerColor = Color(0xFFE6F4F1),
                            disabledContainerColor = Color(0xFFE6F4F1),
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
                        text = "Lugares Populares",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    CustomTabs(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )

                    when (selectedTab) {
                        0 -> ListaContent(
                            lugares = lugares,
                            onLugarClick = onLugarClick
                        )
                        1 -> MapContent()
                    }
                }
            }
        }
    )
}

@Composable
fun LugarCard(
    lugar: Lugar,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = lugar.imageRes),
            contentDescription = lugar.nombre,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(lugar.nombre, fontWeight = FontWeight.Bold)
            Text(lugar.descripcion, color = Color.Gray, fontSize = 13.sp)
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
