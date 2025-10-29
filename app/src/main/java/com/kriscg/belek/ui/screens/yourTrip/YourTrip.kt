package com.kriscg.belek.ui.screens.yourTrip

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.R
import com.kriscg.belek.ui.theme.BelekTheme

@Composable
fun TuViajeScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTipoLugar by remember { mutableStateOf(setOf("Naturales", "Históricos"))}
    var selectedAmbiente by remember { mutableStateOf(setOf("Románticos", "Familiares")) }
    var selectedServicios by remember { mutableStateOf(setOf("Estacionamiento", "Wifi", "Petfriendly") )}
    var selectedPrecio by remember { mutableStateOf(setOf("Gama Media", "Económico"))}
    var selectedMomento by remember { mutableStateOf(setOf("Matutino", "Mediodía")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onBackClick()}
            )
            Text(
                text = "TU VIAJE",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        MultiFilterSection(
            title = "Tipo de Lugar",
            options = listOf(
                "Arqueológicos", "Naturales", "Históricos", "Culturales",
                "Ecoturísticos", "Gastronómicos", "Recreativos", "Comerciales"
            ),
            selectedOptions = selectedTipoLugar,
            onOptionToggled = { option ->
                selectedTipoLugar = if (selectedTipoLugar.contains(option)) {
                    selectedTipoLugar - option
                } else {
                    selectedTipoLugar + option
                }
            }
        )

        MultiFilterSection(
            title = "Ambiente",
            options = listOf(
                "Familiares", "Románticos", "Aventura", "Cultural",
                "Nocturnos", "Espiritual"
            ),
            selectedOptions = selectedAmbiente,
            onOptionToggled = { option ->
                selectedAmbiente = if (selectedAmbiente.contains(option)) {
                    selectedAmbiente - option
                } else {
                    selectedAmbiente + option
                }
            }
        )

        MultiFilterSection(
            title = "Servicios",
            options = listOf(
                "Estacionamiento", "Wifi", "Petfriendly", "Accesible",
                "Transporte Incluido", "Tours Incluidos"
            ),
            selectedOptions = selectedServicios,
            onOptionToggled = { option ->
                selectedServicios = if (selectedServicios.contains(option)) {
                    selectedServicios - option
                } else {
                    selectedServicios + option
                }
            }
        )

        MultiFilterSection(
            title = "Precios",
            options = listOf(
                "Económico", "Gama Media", "Lujo"
            ),
            selectedOptions = selectedPrecio,
            onOptionToggled = { option ->
                selectedPrecio = if (selectedPrecio.contains(option)) {
                    selectedPrecio - option
                } else {
                    selectedPrecio + option
                }
            }
        )

        MultiFilterSection(
            title = "Momento del Día",
            options = listOf(
                "Amanecer", "Matutino", "Mediodía", "Vespertino",
                "Atardecer", "Nocturno"
            ),
            selectedOptions = selectedMomento,
            onOptionToggled = { option ->
                selectedMomento = if (selectedMomento.contains(option)) {
                    selectedMomento - option
                } else {
                    selectedMomento + option
                }
            }
        )


        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PlaceCard(
                    title = "Antigua Guatemala",
                    description = "Ciudad colonial\nrodeada de volcanes.",
                    imageRes = R.drawable.tikal_prueba
                )
            }
            item {
                PlaceCard(
                    title = "Museo Miraflores",
                    description = "Museo sobre ruinas\nmayas con artefactos\nantiguos.",
                    imageRes = R.drawable.tikal_prueba
                )
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
        color = DividerDefaults.color
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
    title: String,
    description: String,
    imageRes: Int
) {
    OutlinedCard(
        modifier = Modifier
            .width(200.dp)
            .height(240.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
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