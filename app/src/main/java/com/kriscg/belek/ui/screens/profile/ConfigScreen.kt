package com.kriscg.belek.ui.screens.profile

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.R
import com.kriscg.belek.data.userpreferences.PreferencesManager
import com.kriscg.belek.ui.theme.BelekTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager.getInstance(context) }
    val preferences by preferencesManager.preferencesFlow.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var publicInfoEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = context.getString(R.string.configuraciones),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = context.getString(R.string.regresar)
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
                    contentDescription = context.getString(R.string.tema_oscuro),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = context.getString(R.string.tema_oscuro),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = preferences.isDarkTheme,
                    onCheckedChange = { preferencesManager.setDarkTheme(it) }
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp))

            CajonesContentClickable(
                icono = Icons.Default.Face,
                opcion = context.getString(R.string.cambiar_idioma),
                valorActual = preferences.languageDisplay,
                onClick = { showLanguageDialog = true }
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp))

            CajonesContentClickable(
                icono = Icons.Default.Settings,
                opcion = context.getString(R.string.moneda_cambio),
                valorActual = preferences.currency,
                onClick = { showCurrencyDialog = true }
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp))

            Text(
                text = context.getString(R.string.privacidad),
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
                    contentDescription = context.getString(R.string.info_publica),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = context.getString(R.string.info_publica),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = publicInfoEnabled,
                    onCheckedChange = { publicInfoEnabled = it }
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp))
        }
    }

    if (showLanguageDialog) {
        val languages = listOf(
            context.getString(R.string.idioma_espanol),
            context.getString(R.string.idioma_ingles)
        )

        SelectionDialog(
            title = context.getString(R.string.seleccionar_idioma),
            options = languages,
            currentSelection = preferences.languageDisplay,
            onDismiss = { showLanguageDialog = false },
            onSelect = { displayName ->
                val code = preferencesManager.getLanguageCode(displayName)
                preferencesManager.setLanguage(code, displayName)
                showLanguageDialog = false

                (context as? ComponentActivity)?.recreate()
            }
        )
    }

    if (showCurrencyDialog) {
        val currencies = listOf(
            context.getString(R.string.moneda_quetzales),
            context.getString(R.string.moneda_dolares)
        )

        SelectionDialog(
            title = context.getString(R.string.seleccionar_moneda),
            options = currencies,
            currentSelection = preferences.currency,
            onDismiss = { showCurrencyDialog = false },
            onSelect = { currency ->
                preferencesManager.setCurrency(currency)
                showCurrencyDialog = false
            }
        )
    }
}

@Composable
fun CajonesContentClickable(
    modifier: Modifier = Modifier,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    opcion: String,
    valorActual: String,
    onClick: () -> Unit
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

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = opcion,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = valorActual,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Cambiar $opcion",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SelectionDialog(
    title: String,
    options: List<String>,
    currentSelection: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                options.forEach { option ->
                    TextButton(
                        onClick = { onSelect(option) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                modifier = Modifier.weight(1f),
                                color = if (option == currentSelection) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                fontWeight = if (option == currentSelection) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                            if (option == currentSelection) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Seleccionado",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    BelekTheme {
        ConfigScreen()
    }
}