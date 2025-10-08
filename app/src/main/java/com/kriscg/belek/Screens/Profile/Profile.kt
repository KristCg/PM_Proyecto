package com.kriscg.belek.Screens.Profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import com.kriscg.belek.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.ui.theme.BelekTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf(0) }

    val favoritos = listOf("Lugar 1", "Lugar 2", "Lugar 3")
    val guardados = listOf("Artículo 1", "Artículo 2")
    val reseñas = listOf("Reseña A", "Reseña B", "Reseña C", "Reseña D")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier.size(125.dp)
                )
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Nombre de Usuario",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed."
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.90F)
                    .padding(10.dp)
            )

            SingleChoiceSegmentedButtonRow {
                SegmentedButton(
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text("Favoritos") }
                )

                SegmentedButton(
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                    icon = { Icon(Icons.Default.Done, contentDescription = null) },
                    label = { Text("Guardados") }
                )

                SegmentedButton(
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2 },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Reseñas") }
                )
            }
            when (selectedIndex) {
                0 -> FavoritosContent(favoritos)
                1 -> GuardadosContent(guardados)
                2 -> ReseñasContent(reseñas)
            }
        }
    }
}

@Composable
fun FavoritosContent(favoritos: List<String>) {
    ContenidoLista(
        items = favoritos,
        bordeColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun GuardadosContent(guardados: List<String>) {
    ContenidoLista(
        items = guardados,
        bordeColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun ReseñasContent(reseñas: List<String>) {
    Surface(
        modifier = Modifier
            .fillMaxSize(0.9f)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {
            items(reseñas.size) { index ->
                Column() {
                    Text(
                        text = reseñas[index],
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    )
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "1 Star",
                            tint = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "1 Star",
                            tint = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "1 Star",
                            tint = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "1 Star",
                            tint = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "1 Star",
                            tint = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(1f),
                        )
                }
            }
        }
    }
}

@Composable
fun ContenidoLista(items: List<String>, bordeColor: Color) {
    Surface(
        modifier = Modifier
            .fillMaxSize(0.9f)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, bordeColor),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = items[index],
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.prueba_guardados_favoritos),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(30.dp))
                    )
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth(1f),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreenContent() {
    BelekTheme {
        ProfileScreen(modifier = Modifier)
    }
}

