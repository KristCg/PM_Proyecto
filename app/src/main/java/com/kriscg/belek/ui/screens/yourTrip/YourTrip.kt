package com.kriscg.belek.ui.screens.yourTrip

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kriscg.belek.R
import com.kriscg.belek.ui.theme.BelekTheme
import com.kriscg.belek.ui.viewModel.YourTripViewModel
import com.kriscg.belek.domain.Lugar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuViajeScreen(
    onBackClick: () -> Unit = {},
    onLugarClick: (Int) -> Unit = {},
    viewModel: YourTripViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TU VIAJE",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            MultiFilterSection(
                title = "Tipo de Lugar",
                options = listOf(
                    "Arqueológicos", "Naturales", "Históricos", "Culturales",
                    "Ecoturísticos", "Gastronómicos", "Recreativos", "Comerciales"
                ),
                selectedOptions = uiState.tiposSeleccionados,
                onOptionToggled = { viewModel.onTipoToggled(it) }
            )

            MultiFilterSection(
                title = "Ambiente",
                options = listOf(
                    "Familiares", "Románticos", "Aventura", "Cultural",
                    "Nocturnos", "Espiritual"
                ),
                selectedOptions = uiState.ambientesSeleccionados,
                onOptionToggled = { viewModel.onAmbienteToggled(it) }
            )

            MultiFilterSection(
                title = "Servicios",
                options = listOf(
                    "Estacionamiento", "Wifi", "Petfriendly", "Accesible",
                    "Transporte Incluido", "Tours Incluidos"
                ),
                selectedOptions = uiState.serviciosSeleccionados,
                onOptionToggled = { viewModel.onServicioToggled(it) }
            )

            MultiFilterSection(
                title = "Precios",
                options = listOf(
                    "Económico", "Gama Media", "Lujo"
                ),
                selectedOptions = uiState.preciosSeleccionados,
                onOptionToggled = { viewModel.onPrecioToggled(it) }
            )

            MultiFilterSection(
                title = "Momento del Día",
                options = listOf(
                    "Amanecer", "Matutino", "Mediodía", "Vespertino",
                    "Atardecer", "Nocturno"
                ),
                selectedOptions = uiState.momentosSeleccionados,
                onOptionToggled = { viewModel.onMomentoToggled(it) }
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else if (uiState.lugaresRecomendados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron lugares con los filtros seleccionados",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.lugaresRecomendados) { lugar ->
                        PlaceCard(
                            lugar = lugar,
                            onClick = { onLugarClick(lugar.id ?: 0) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MultiFilterSection(
    title: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionToggled: (String) -> Unit
) {
    HorizontalDivider(
        thickness = DividerDefaults.Thickness,
        color = MaterialTheme.colorScheme.outlineVariant
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(options) { option ->
                FilterChip(
                    selected = selectedOptions.contains(option),
                    onClick = { onOptionToggled(option) },
                    label = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun PlaceCard(
    lugar: Lugar,
    onClick: () -> Unit = {}
) {
    OutlinedCard(
        modifier = Modifier
            .width(200.dp)
            .height(240.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (lugar.imagenUrl != null) {
                AsyncImage(
                    model = lugar.imagenUrl,
                    contentDescription = lugar.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.tikal_prueba)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.tikal_prueba),
                    contentDescription = lugar.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = lugar.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = lugar.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp,
                    maxLines = 3
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTuViajeScreen() {
    BelekTheme {
        TuViajeScreen()
    }
}