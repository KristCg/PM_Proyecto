package com.kriscg.belek.Screens.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.ui.theme.BelekTheme
import kotlinx.coroutines.launch

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val isHeader: Boolean = false,
    val showDivider: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDrawer(
    modifier: Modifier = Modifier,
    onMenuItemClick: (String) -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        MenuItem("Nombre de Usuario", Icons.Default.Person, isHeader = true),
        MenuItem("Ver perfil", Icons.Default.AccountCircle),
        MenuItem("Agregar otra cuenta", Icons.Default.Add, showDivider = true),
        MenuItem("Historial", Icons.Default.Menu),
        MenuItem("Configuración y privacidad", Icons.Default.Settings, showDivider = true),
        MenuItem("Cerrar Sesión", Icons.Default.ExitToApp),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
            ) {
                DrawerHeader()

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    menuItems.forEachIndexed { index, item ->
                        if (item.isHeader) {
                            DrawerHeaderItem(item)
                        } else {
                            NavigationDrawerItem(
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        fontSize = 14.sp
                                    )
                                },
                                selected = false,
                                onClick = {
                                    onMenuItemClick(item.title)
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(
                                    NavigationDrawerItemDefaults.ItemPadding
                                )
                            )
                        }

                        if (item.showDivider) {
                            Divider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        },
        content = {
            MainContent(
                onOpenDrawer = {
                    scope.launch { drawerState.open() }
                }
            )
        }
    )
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
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
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bienvenido",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DrawerHeaderItem(item: MenuItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun MainContent(
    onOpenDrawer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .clickable { onOpenDrawer() }
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Abrir menú",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Abrir Menú",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SimpleProfileMenu(
    onMenuItemClick: (String) -> Unit = {}
) {
    val menuItems = listOf(
        "Nombre de Usuario" to Icons.Default.Person,
        "Ver perfil" to Icons.Default.AccountCircle,
        "Agregar otra cuenta" to Icons.Default.Add,
        "Historial" to Icons.Default.Menu,
        "Configuración y privacidad" to Icons.Default.Settings,
        "Cerrar Sesión" to Icons.Default.ExitToApp,
    )

    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Usuario",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Nombre de Usuario",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Items del menú
        menuItems.forEachIndexed { index, (title, icon) ->
            if (index == 2 || index == 4) { // Divisores después de "Agregar otra cuenta" y "Configuración y privacidad"
                Divider(modifier = Modifier.padding(vertical = 8.dp))
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

@Preview(showBackground = true)
@Composable
fun ProfileDrawerPreview() {
    BelekTheme {
        ProfileDrawer()
    }
}

@Preview
@Composable
fun SimpleProfileMenuPreview() {
    BelekTheme {
        SimpleProfileMenu()
    }
}