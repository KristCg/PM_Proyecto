package com.kriscg.belek.ui.screens.encuestas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kriscg.belek.R
import com.kriscg.belek.ui.viewModel.EncuestaViewModel

val tipos = listOf(
    "Arqueológicos",
    "Naturales",
    "Históricos",
    "Culturales",
    "Ecoturísticos",
    "Gastronómicos",
    "Recreativos",
    "Comerciales"
)

val presupuesto = listOf("Alto", "Mediano", "Bajo")
val intereses = listOf("Gastronomía", "Fotografía", "Artesanías y compras", "Aventura", "Relajación")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncuestaScreen(
    modifier: Modifier = Modifier,
    onVerOpcionesClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: EncuestaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.viajeCreado) {
        if (uiState.viajeCreado) {
            onVerOpcionesClick()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nuevo Viaje",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar"
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
                    .fillMaxWidth()
                    .padding(16.dp),
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
                    text = "Destino de viaje:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                TextField(
                    value = uiState.destino,
                    onValueChange = { viewModel.onDestinoChange(it) },
                    shape = RoundedCornerShape(10.dp),
                    placeholder = { Text("Dirección") },
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

                Image(
                    painter = painterResource(id = R.drawable.destino),
                    contentDescription = "Destino",
                    contentScale = ContentScale.Fit,
                    alpha = 0.3f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(Modifier.height(30.dp))

                Text(
                    text = "Tipo de viaje:",
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
                    text = "Presupuesto:",
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
                    text = "Intereses específicos:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(intereses) { interes ->
                        FiltroBoton(
                            texto = interes,
                            seleccionado = uiState.interesesSeleccionados.contains(interes),
                            onClick = { viewModel.onInteresToggled(interes) },
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
                            text = "Ver opciones",
                            fontSize = 15.sp
                        )
                    }
                }
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