package com.kriscg.belek.ui.screens.details

import com.kriscg.belek.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kriscg.belek.ui.theme.BelekTheme

@Composable
fun CustomTabs(
    tabs: List<String> = listOf("Descripción", "Mapa", "Calificaciones"),
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
                            0 -> RoundedCornerShape(
                                topStart = 26.dp,
                                bottomStart = 26.dp
                            )
                            tabs.size - 1 -> RoundedCornerShape(
                                topEnd = 26.dp,
                                bottomEnd = 26.dp
                            )
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

@Composable
fun DetailsContent (
){
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                onClick = {},
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )
            }

            Text(
                text = "DETALLES",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider(
            modifier = Modifier,
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )

        Image(
            painterResource(R.drawable.tikal_prueba),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp)
                .clip(shape = RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ){
            Text(
                text = "Tikal, Guatemala",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Parque Nacional Tikal, Departamento de Petén, Guatemala.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "De 6:00 a.m. a 5:00 p.m",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        CustomTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }

        ); when (selectedTab) {
            0 -> DescriptionContent()
            1 -> MapContent()
            2 -> RatingsContent()
        }
    }
}

@Composable
fun DescriptionContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ){
        Text(
            text = "Acerca de Tikal",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "Tikal es una antigua ciudadela maya en los bosques " +
                    "tropicales del norte de Guatemala. Se cree que data" +
                    " del siglo I d.C. Tikal floreció entre los años" +
                    " 200 y 850 d.C. y luego fue abandonada. Sus ruinas" +
                    " icónicas de templos y palacios incluyen la gigante " +
                    "pirámide ceremonial Mundo Perdido y el Templo del Gran" +
                    " Jaguar. A 70 metros, el Templo IV es la estructura " +
                    "precolombina más alta de América y tiene vista panorámica.",
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = "Precios",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "Entrada para extranjeros: Q150\n" +
                    "Entrada para centroamericanos: Q75.\n" +
                    "Guatemaltecos: Q25.\n" +
                    "Tour de Amanecer: Precio adicional de Q100 \n" +
                    "Guía certificado: Desde Q200 por grupo para un tour de 3-4 horas.",
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun MapContent() {
    Text("Contenido del Mapa")
}

@Composable
fun RatingsContent() {
    Text("Contenido de Calificaciones")
}

@Preview
@Composable
fun PreviewDetailsScreen(){
    BelekTheme {
        DetailsContent()
    }
}