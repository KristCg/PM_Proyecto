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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    lugarId: Int = 0,
    onBackClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showReviewDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "DETALLES",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineMedium,
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
        },
        floatingActionButton = {
            if (selectedTab == 2) {
                FloatingActionButton(
                    onClick = { showReviewDialog = true },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar reseña"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painterResource(R.drawable.tikal_prueba),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(shape = RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Tikal, Guatemala",
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
                        text = "Parque Nacional Tikal, Departamento de Petén, Guatemala.",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

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
                        text = "De 6:00 a.m. a 5:00 p.m",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            CustomTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            when (selectedTab) {
                0 -> DescriptionContent()
                1 -> MapContent()
                2 -> RatingsContent()
            }
        }
    }

    if (showReviewDialog) {
        AddReviewDialog(
            onDismiss = { showReviewDialog = false },
            onSubmit = { rating, comment ->
                showReviewDialog = false
            }
        )
    }
}

@Composable
fun AddReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (Int, String) -> Unit
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
                            text = "María Aguilar",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
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
                    label = { Text("Comentario") },
                    placeholder = { Text("Cuéntanos que te pareció el lugar") },
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
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
                        text = "Dejanos tu calificación",
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
                                    .clickable { rating = index + 1 }
                            )
                        }
                    }
                }

                TextButton(
                    onClick = {
                        if (rating > 0 && comment.isNotEmpty()) {
                             onSubmit(rating, comment)
                         }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                     Text("Enviar")
                }
            }
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
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painterResource(R.drawable.tikal_mapa),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun RatingsContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
                            text = "María Aguilar",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "12/09/2025",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(4) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Es un lugar muy bonito y organizado, recomiendo estar bien hidratado, llevar repelente, bloqueador solar y un sombrero, hace demasiado calor, estuvimos a 42°C pero vale la pena, es hermoso.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

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
                            text = "Antonio Zabaleta",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "12/09/2025",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(2) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    repeat(3) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDetailsScreen(){
    BelekTheme {
        DetailsScreen()
    }
}