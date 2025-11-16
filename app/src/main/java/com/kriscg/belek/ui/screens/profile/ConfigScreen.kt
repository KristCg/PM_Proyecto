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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.R
import com.kriscg.belek.data.userpreferences.PreferencesManager

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.configuraciones),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = stringResource(R.string.regresar)
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
            // Tema Oscuro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.tema_oscuro),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = stringResource(R.string.tema_oscuro),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = preferences.isDarkTheme,
                    onCheckedChange = { preferencesManager.setDarkTheme(it) }
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp))

            // Cambiar Idioma
            CajonesContentClickable(
                icono = Icons.Default.Face,
                opcion = stringResource(R.string.cambiar_idioma),
                valorActual = preferences.languageDisplay,
                onClick = { showLanguageDialog = true }
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp))

            // Moneda de cambio
            CajonesContentClickable(
                icono = Icons.Default.Settings,
                opcion = stringResource(R.string.moneda_cambio),
                valorActual = getCurrencyDisplay(preferences.currency),
                onClick = { showCurrencyDialog = true }
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp))

            // Privacidad
            Text(
                text = stringResource(R.string.privacidad),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = TextAlign.Start,
                fontSize = 20.sp
            )

            // Info Pública
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.info_publica),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = stringResource(R.string.info_publica),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = preferences.isPublicInfoEnabled,
                    onCheckedChange = { preferencesManager.setPublicInfo(it) }
                )
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 16.dp))
        }
    }

    // Dialog de idioma
    if (showLanguageDialog) {
        val languages = listOf(
            stringResource(R.string.idioma_espanol),
            stringResource(R.string.idioma_ingles)
        )

        SelectionDialog(
            title = stringResource(R.string.seleccionar_idioma),
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

    // Dialog de moneda
    if (showCurrencyDialog) {
        val currencies = listOf("Q", "$")

        SelectionDialog(
            title = stringResource(R.string.seleccionar_moneda),
            options = currencies.map { getCurrencyDisplay(it) },
            currentSelection = getCurrencyDisplay(preferences.currency),
            onDismiss = { showCurrencyDialog = false },
            onSelect = { display ->
                val symbol = when {
                    display.contains("Quetzales") -> "Q"
                    display.contains("Dollars") || display.contains("Dólares") -> "$"
                    else -> "Q"
                }
                preferencesManager.setCurrency(symbol)
                showCurrencyDialog = false
            }
        )
    }
}

@Composable
private fun getCurrencyDisplay(symbol: String): String {
    val context = LocalContext.current
    val currentLanguage = PreferencesManager.getInstance(context).preferencesFlow.collectAsState().value.language

    return when (symbol) {
        "Q" -> if (currentLanguage == "en") "Quetzales (Q)" else "Quetzales (Q)"
        "$" -> if (currentLanguage == "en") "Dollars ($)" else "Dólares ($)"
        else -> "Quetzales (Q)"
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
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}