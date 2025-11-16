package com.kriscg.belek.ui.screens.yourTrip

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

private const val TAG = "TuViajeScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuViajeScreen(
    tipo: String? = null,
    presupuesto: String? = null,
    ambientesJson: String? = null,
    onBackClick: () -> Unit = {},
    onLugarClick: (Int) -> Unit = {},
    viewModel: YourTripViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        Log.d(TAG, "========================================")
        Log.d(TAG, "INICIANDO TuViajeScreen")
        Log.d(TAG, "Tipo recibido: $tipo")
        Log.d(TAG, "Presupuesto recibido: $presupuesto")
        Log.d(TAG, "AmbientesJson recibido: $ambientesJson")
        Log.d(TAG, "========================================")

        if (tipo != null || presupuesto != null || ambientesJson != null) {
            val ambientes = if (!ambientesJson.isNullOrBlank()) {
                try {
                    Log.d(TAG, "Intentando deserializar JSON: $ambientesJson")
                    val decoded = kotlinx.serialization.json.Json.decodeFromString<List<String>>(ambientesJson)
                    Log.d(TAG, "✅ Deserialización exitosa: $decoded")
                    decoded.toSet()
                } catch (e: Exception) {
                    Log.e(TAG, "❌ ERROR al deserializar: ${e.message}")
                    emptySet()
                }
            } else {
                Log.d(TAG, "AmbientesJson vacío o null")
                emptySet()
            }

            Log.d(TAG, "Ambientes finales: $ambientes")
            Log.d(TAG, "Llamando a initializeFromEncuesta...")
            viewModel.initializeFromEncuesta(tipo, presupuesto, ambientes)
        }
    }

    LaunchedEffect(uiState.ambientesSeleccionados) {
        Log.d(TAG, "Estado actualizado - Ambientes seleccionados: ${uiState.ambientesSeleccionados}")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.tu_viaje),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.volver)
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
                title = stringResource(R.string.tipo_lugar),
                options = listOf(
                    stringResource(R.string.arqueologicos),
                    stringResource(R.string.naturales),
                    stringResource(R.string.historicos),
                    stringResource(R.string.culturales),
                    stringResource(R.string.ecoturisticos),
                    stringResource(R.string.gastronomicos),
                    stringResource(R.string.recreativos),
                    stringResource(R.string.comerciales)
                ),
                selectedOptions = uiState.tiposSeleccionados,
                onOptionToggled = { viewModel.onTipoToggled(it) }
            )

            MultiFilterSection(
                title = stringResource(R.string.ambiente),
                options = listOf(
                    stringResource(R.string.familiares),
                    stringResource(R.string.romanticos),
                    stringResource(R.string.aventura),
                    stringResource(R.string.cultural),
                    stringResource(R.string.nocturnos),
                    stringResource(R.string.espiritual)
                ),
                selectedOptions = uiState.ambientesSeleccionados,
                onOptionToggled = {
                    Log.d(TAG, "Toggle ambiente clickeado: $it")
                    Log.d(TAG, "Estado actual: ${uiState.ambientesSeleccionados}")
                    viewModel.onAmbienteToggled(it)
                }
            )

            MultiFilterSection(
                title = stringResource(R.string.servicios),
                options = listOf(
                    stringResource(R.string.estacionamiento),
                    stringResource(R.string.wifi),
                    stringResource(R.string.petfriendly),
                    stringResource(R.string.accesible),
                    stringResource(R.string.transporte_incluido),
                    stringResource(R.string.tours_incluidos)
                ),
                selectedOptions = uiState.serviciosSeleccionados,
                onOptionToggled = { viewModel.onServicioToggled(it) }
            )

            MultiFilterSection(
                title = stringResource(R.string.precios),
                options = listOf(
                    stringResource(R.string.economico),
                    stringResource(R.string.gama_media),
                    stringResource(R.string.lujo)
                ),
                selectedOptions = uiState.preciosSeleccionados,
                onOptionToggled = { viewModel.onPrecioToggled(it) }
            )

            MultiFilterSection(
                title = stringResource(R.string.momento_dia),
                options = listOf(
                    stringResource(R.string.amanecer),
                    stringResource(R.string.matutino),
                    stringResource(R.string.mediodia),
                    stringResource(R.string.vespertino),
                    stringResource(R.string.atardecer),
                    stringResource(R.string.nocturno)
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
                        text = uiState.error ?: stringResource(R.string.error_desconocido),
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
                        text = stringResource(R.string.no_lugares_filtros),
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

        if (title == stringResource(R.string.ambiente)) {
            Log.d(TAG, "Renderizando $title - Seleccionados: $selectedOptions")
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(options) { option ->
                val isSelected = selectedOptions.contains(option)

                if (title == stringResource(R.string.ambiente)) {
                    Log.d(TAG, "Chip '$option' - isSelected: $isSelected")
                }

                FilterChip(
                    selected = isSelected,
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