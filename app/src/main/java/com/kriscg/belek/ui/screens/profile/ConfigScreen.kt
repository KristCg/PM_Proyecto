package com.kriscg.belek.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.ui.theme.BelekTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var publicInfoEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuraciones",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {onBackClick}) {
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
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Tema oscuro",
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = "Tema Oscuro",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = darkThemeEnabled,
                    onCheckedChange = { darkThemeEnabled = it }
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp),)


            CajonesContent(
                icono = Icons.Default.Face,
                opcion = "Cambiar Idioma"
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp),)


            CajonesContent(
                icono = Icons.Default.Settings,
                opcion = "Moneda de cambio"
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp),)
            Text(
                text = "Privacidad",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = TextAlign.Start,
                fontSize = 20.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Información pública",
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = "Información usuario pública",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = publicInfoEnabled,
                    onCheckedChange = { publicInfoEnabled = it }
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp),)

        }
    }
}

@Composable
fun CajonesContent(
    modifier: Modifier = Modifier,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    opcion: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = "Icono de $opcion",
            modifier = Modifier.padding(end = 16.dp)
        )

        Text(
            text = opcion,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Desplegar opciones",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    BelekTheme {
        ConfigScreen()
    }
}