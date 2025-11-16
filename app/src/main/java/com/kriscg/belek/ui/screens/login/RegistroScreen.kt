package com.kriscg.belek.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kriscg.belek.R
import com.kriscg.belek.ui.viewModel.RegistroViewModel

@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    onRegistroClick: () -> Unit = {},
    viewModel: RegistroViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registroSuccess) {
        if (uiState.registroSuccess) {
            onRegistroClick()
            viewModel.resetState()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                alpha = 0.3f
            )

            Text(
                text = stringResource(R.string.registro),
                fontSize = 40.sp
            )

            Spacer(Modifier.height(32.dp))

            if (uiState.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
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

            TextField(
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChange(it) },
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text(stringResource(R.string.usuario)) },
                singleLine = true,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(Modifier.height(20.dp))

            TextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text(stringResource(R.string.correo)) },
                singleLine = true,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(Modifier.height(20.dp))

            TextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text(stringResource(R.string.password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.register() },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6650a4)
                ),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = stringResource(R.string.registrarse),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}