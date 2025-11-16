package com.kriscg.belek.ui.screens.details

import com.kriscg.belek.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kriscg.belek.ui.viewModel.DetailsViewModel
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.domain.Resena
import com.kriscg.belek.util.TranslationHelper

@Composable
fun CustomTabs(
    tabs: List<String>,
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(horizontal = 16.dp)
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
                        color = if (index == selectedTab) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSecondary
                        },
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
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    lugarId: Int = 0,
    onBackClick: () -> Unit = {},
    viewModel: DetailsViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val preferencesManager = remember {
        com.kriscg.belek.data.userpreferences.PreferencesManager.getInstance(context)
    }
    val preferences by preferencesManager.preferencesFlow.collectAsState()
    val currentLanguage = preferences.language

    LaunchedEffect(lugarId) {
        viewModel.loadLugarDetails(lugarId)
    }

    LaunchedEffect(uiState.reviewSubmitted) {
        if (uiState.reviewSubmitted) {
            viewModel.hideReviewDialog()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.detalles),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineMedium,
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
        },
        floatingActionButton = {
            if (uiState.selectedTab == 2) {
                FloatingActionButton(
                    onClick = { viewModel.showReviewDialog() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Draw,
                        contentDescription = stringResource(R.string.agregar_resena)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.error ?: stringResource(R.string.error_desconocido),
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.loadLugarDetails(lugarId) }) {
                        Text(stringResource(R.string.reintentar))
                    }
                }
            }
        } else if (uiState.lugar != null) {
            val lugar = uiState.lugar!!
            val nombreTraducido = TranslationHelper.getLugarNombre(lugar, currentLanguage)
            val descripcionTraducida = TranslationHelper.getLugarDescripcion(lugar, currentLanguage)
            val precioTraducido = if (currentLanguage == "en") lugar.precioEn else lugar.precio

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (lugar.imagenUrl != null) {
                    AsyncImage(
                        model = lugar.imagenUrl,
                        contentDescription = nombreTraducido,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp)
                            .clip(shape = RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painterResource(R.drawable.tikal_prueba),
                        contentDescription = nombreTraducido,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp)
                            .clip(shape = RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = nombreTraducido,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = lugar.ubicacion,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    if (lugar.horario != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = lugar.horario,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                CustomTabs(
                    tabs = listOf(
                        stringResource(R.string.descripcion),
                        stringResource(R.string.mapa),
                        stringResource(R.string.calificaciones)
                    ),
                    selectedTab = uiState.selectedTab,
                    onTabSelected = { viewModel.onTabSelected(it) }
                )

                when (uiState.selectedTab) {
                    0 -> DescriptionContent(
                        descripcion = descripcionTraducida,
                        precio = precioTraducido
                    )
                    1 -> MapContent(lugar = lugar)
                    2 -> RatingsContent(resenas = uiState.resenas)
                }
            }
        }
    }

    if (uiState.showReviewDialog) {
        AddReviewDialog(
            onDismiss = { viewModel.hideReviewDialog() },
            onSubmit = { rating, comment ->
                viewModel.submitReview(rating, comment)
            },
            isSubmitting = uiState.isSubmittingReview
        )
    }
}

@Composable
fun AddReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (Int, String) -> Unit,
    isSubmitting: Boolean = false
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Text(
                            text = stringResource(R.string.tu_resena),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onDismiss, enabled = !isSubmitting) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.cerrar),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    label = { Text(stringResource(R.string.comentario)) },
                    placeholder = { Text(stringResource(R.string.placeholder_comentario)) },
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isSubmitting,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                    )
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.calificacion_text),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Estrella ${index + 1}",
                                tint = if (index < rating) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.LightGray
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable(enabled = !isSubmitting) {
                                        rating = index + 1
                                    }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (rating > 0 && comment.isNotEmpty()) {
                            onSubmit(rating, comment)
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = !isSubmitting && rating > 0 && comment.isNotEmpty()
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(stringResource(R.string.enviar))
                    }
                }
            }
        }
    }
}

@Composable
fun DescriptionContent(
    descripcion: String,
    precio: String?
) {
    val context = LocalContext.current
    val preferencesManager = remember {
        com.kriscg.belek.data.userpreferences.PreferencesManager.getInstance(context)
    }
    val preferences by preferencesManager.preferencesFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.descripcion),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = descripcion,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium
        )

        if (precio != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.precios),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = convertCurrency(precio, preferences.currency),
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

// Función para convertir moneda
private fun convertCurrency(precio: String, targetCurrency: String): String {
    // Tasa de conversión aproximada Q7.8 = $1
    val conversionRate = 7.8

    return try {
        // Extraer números del precio
        val numbers = precio.replace(Regex("[^0-9.]"), "").toDoubleOrNull()

        if (numbers != null) {
            when {
                precio.contains("Q") && targetCurrency == "$" -> {
                    val converted = numbers / conversionRate
                    precio.replace(Regex("Q\\s*[0-9.]+"), "%.2f".format(converted))
                }
                precio.contains("$") && targetCurrency == "Q" -> {
                    val converted = numbers * conversionRate
                    precio.replace(Regex("\\$\\s*[0-9.]+"), "Q${"%.2f".format(converted)}")
                }
                !precio.contains("Q") && !precio.contains("$") -> {
                    // Si no tiene símbolo, asumir que está en Q y convertir según preferencia
                    if (targetCurrency == "$") {
                        val converted = numbers / conversionRate
                        "%.2f".format(converted)
                    } else {
                        "Q$precio"
                    }
                }
                else -> precio
            }
        } else {
            precio
        }
    } catch (_: Exception) {
        precio
    }
}

@Composable
fun MapContent(lugar: Lugar) {
    val defaultLocation = LatLng(14.6349, -90.5069)

    val location = remember(lugar.latitud, lugar.longitud) {
        val lat = lugar.latitud
        val long = lugar.longitud
        if (lat != null && long != null) {
            LatLng(lat, long)
        } else {
            defaultLocation
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 14f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        GoogleMapView(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            markerPosition = location,
            markerTitle = lugar.nombre
        )
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    markerPosition: LatLng? = null,
    markerTitle: String? = null
) {
    val markerState = markerPosition?.let {
        rememberMarkerState(position = it)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        if (markerState != null) {
            Marker(
                state = markerState,
                title = markerTitle
            )
        }
    }
}

@Composable
fun RatingsContent(
    resenas: List<Resena>
) {
    val context = LocalContext.current
    val preferencesManager = remember {
        com.kriscg.belek.data.userpreferences.PreferencesManager.getInstance(context)
    }
    val preferences by preferencesManager.preferencesFlow.collectAsState()

    if (resenas.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_resenas),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(resenas) { resena ->
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = if (preferences.isPublicInfoEnabled) {
                                        "Usuario ${resena.usuarioId.take(8)}"
                                    } else {
                                        "Usuario Anónimo"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = resena.createdAt?.let {
                                        formatDate(it)
                                    } ?: "Fecha desconocida",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < resena.calificacion) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        Color.LightGray
                                    },
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Text(
                            text = resena.comentario,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (_: Exception) {
        dateString
    }
}