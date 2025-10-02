package com.kriscg.belek.Screens.Profile

import android.R
import androidx.compose.animation.core.estimateAnimationDurationMillis
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar perfil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Perfil",
                modifier = Modifier
                    .size(175.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Button(
                onClick = {})
            {
                Text(
                    text = "Cambiar Imagen"

                )
            }
            Spacer(modifier = Modifier.height(25.dp))

            InfoContent(
                titulo = "Nombre de Usuario",
                contenido = "Nombre_de_usuario"
            )
            InfoContent(
                titulo = "Correo Electronico",
                contenido = "CorreoUsuario@example.com"
            )
            InfoContent(
                titulo = "Descripción",
                contenido = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
            InfoContent(
                titulo = "Contraseña",
                contenido = "Contraseñaejemplo"
            )


        }
    }
}

@Composable
fun InfoContent(
    modifier: Modifier = Modifier,
    titulo: String,
    contenido: String
) {
    val mostrarContenido = if (titulo.equals("Contraseña", ignoreCase = true)) {
        "*".repeat(contenido.length.coerceAtLeast(6))
    } else {
        contenido
    }
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = titulo,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 17.sp
            )
            Box(
                modifier = Modifier
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Editar"
                    )
                }
            }
        }
        Text(
            text = mostrarContenido,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 17.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreenContent() {
    MaterialTheme {
        EditProfileScreen(modifier = Modifier)
    }
}