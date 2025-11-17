package com.kriscg.belek.ui.screens.encuestas

import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.kriscg.belek.R
import com.kriscg.belek.ui.viewModel.EncuestaViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.launch
import java.util.Locale
import com.kriscg.belek.rememberUserLocation
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import com.google.android.gms.maps.CameraUpdateFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncuestaScreen(
    modifier: Modifier = Modifier,
    onVerOpcionesClick: (
        tipo: String?,
        presupuesto: String?,
        ambientes: String?,
        departamento: String?
    ) -> Unit = { _, _, _, _ -> },
    onBackClick: () -> Unit = {},
    viewModel: EncuestaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var destinoCoords by remember { mutableStateOf<LatLng?>(null) }
    val userLocation = rememberUserLocation()

    val tipos = listOf(
        stringResource(R.string.arqueologicos),
        stringResource(R.string.naturales),
        stringResource(R.string.historicos),
        stringResource(R.string.culturales),
        stringResource(R.string.ecoturisticos),
        stringResource(R.string.gastronomicos),
        stringResource(R.string.recreativos),
        stringResource(R.string.comerciales)
    )

    val presupuesto = listOf(
        stringResource(R.string.economico),
        stringResource(R.string.gama_media),
        stringResource(R.string.lujo)
    )

    val ambientes = listOf(
        stringResource(R.string.familiares),
        stringResource(R.string.romanticos),
        stringResource(R.string.aventura),
        stringResource(R.string.cultural),
        stringResource(R.string.nocturnos),
        stringResource(R.string.espiritual)
    )

    LaunchedEffect(uiState.viajeCreado) {
        if (uiState.viajeCreado) {
            val ambientesJson = if (uiState.ambientesSeleccionados.isNotEmpty()) {
                val json = Json.encodeToString(uiState.ambientesSeleccionados.toList())
                println("DEBUG Encuesta: Ambientes seleccionados: ${uiState.ambientesSeleccionados}")
                println("DEBUG Encuesta: JSON generado: $json")
                json
            } else {
                println("DEBUG Encuesta: No hay ambientes seleccionados")
                null
            }

            println("DEBUG Encuesta: Navegando con - Tipo: ${uiState.tipoSeleccionado}, Presupuesto: ${uiState.presupuestoSeleccionado}, Ambientes: $ambientesJson")

            onVerOpcionesClick(
                uiState.tipoSeleccionado,
                uiState.presupuestoSeleccionado,
                ambientesJson,
                uiState.destinoDepartamento
            )
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.nuevo_viaje),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = stringResource(R.string.volver)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
            ) {
                if (uiState.error != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.destino_viaje),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                TextField(
                    value = uiState.destino,
                    onValueChange = { viewModel.onDestinoChange(it) },
                    shape = RoundedCornerShape(10.dp),
                    placeholder = { Text("Formato: lugar, departamento, pais") },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            val destinoTexto = uiState.destino
                            if (destinoTexto.isNotBlank()) {
                                scope.launch {
                                    try {
                                        val geocoder = Geocoder(context, Locale.getDefault())
                                        val results = geocoder.getFromLocationName(destinoTexto, 1)
                                        val location = results?.firstOrNull()
                                        if (location != null) {
                                            destinoCoords = LatLng(location.latitude, location.longitude)
                                            val departamento = location.adminArea

                                            viewModel.onDestinoDepartamento(departamento)
                                            viewModel.clearError()
                                        } else {
                                            viewModel.setError("No se encontró la dirección")
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        viewModel.setError("Error al obtener la dirección")
                                    }
                                }
                            }
                        },
                        enabled = !uiState.isLoading
                    ) {
                        Text(text = "Buscar")
                    }

                    Spacer(Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (userLocation != null) {
                                destinoCoords = userLocation
                                viewModel.clearError()
                            } else {
                                viewModel.setError("No se pudo obtener tu ubicación")
                            }
                        },
                        enabled = !uiState.isLoading
                    ) {
                        Text(text = "Usar mi ubicación")
                    }
                }

                Spacer(Modifier.height(10.dp))

                EncuestaMap(destinoCoords = destinoCoords)

                Spacer(Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.tipo_viaje),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tipos) { tipo ->
                        FiltroBoton(
                            texto = tipo,
                            seleccionado = uiState.tipoSeleccionado == tipo,
                            onClick = { viewModel.onTipoSelected(tipo) },
                            enabled = !uiState.isLoading
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.presupuesto),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(presupuesto) { pres ->
                        FiltroBoton(
                            texto = pres,
                            seleccionado = uiState.presupuestoSeleccionado == pres,
                            onClick = { viewModel.onPresupuestoSelected(pres) },
                            enabled = !uiState.isLoading
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.ambiente_deseado),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ambientes) { ambiente ->
                        FiltroBoton(
                            texto = ambiente,
                            seleccionado = uiState.ambientesSeleccionados.contains(ambiente),
                            onClick = { viewModel.onAmbienteToggled(ambiente) },
                            enabled = !uiState.isLoading
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = { viewModel.guardarViaje() },
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.ver_opciones),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun EncuestaMap(destinoCoords: LatLng?) {
    val defaultLocation = LatLng(14.6349, -90.5069)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    val pin = destinoCoords ?: defaultLocation

    LaunchedEffect(pin) {
        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(pin, 14f))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState
        ){
            if (destinoCoords != null) {
                val markerState = rememberMarkerState(position = destinoCoords)
                Marker(
                    state = markerState
                )
            }
        }
    }
}


@Composable
private fun FiltroBoton(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (seleccionado)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = texto,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EncuestaPreview() {
    MaterialTheme {
        EncuestaScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}