package com.kriscg.belek.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kriscg.belek.R
import com.kriscg.belek.ui.viewModel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearSuccess()
            viewModel.loadProfile()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.editar_perfil),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = stringResource(R.string.volver)
                        )
                    }
                }
            )
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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.perfil),
                    modifier = Modifier.size(175.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(25.dp))

                if (uiState.error != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 8.dp),
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

                if (uiState.updateSuccess) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.actualizado_exitosamente),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                EditableInfoContent(
                    titulo = stringResource(R.string.nombre_usuario),
                    contenido = uiState.usuario?.username ?: "",
                    isEditing = uiState.isEditingUsername,
                    onEdit = { viewModel.startEditingUsername() },
                    onSave = { newValue ->
                        viewModel.updateUsername(newValue)
                    },
                    onCancel = { viewModel.cancelEditing() }
                )

                EditableInfoContent(
                    titulo = stringResource(R.string.correo),
                    contenido = uiState.usuario?.email ?: "",
                    isEditing = uiState.isEditingEmail,
                    onEdit = { viewModel.startEditingEmail() },
                    onSave = { newValue ->
                        viewModel.updateEmail(newValue)
                    },
                    onCancel = { viewModel.cancelEditing() }
                )

                EditableInfoContent(
                    titulo = stringResource(R.string.descripcion_perfil),
                    contenido = uiState.usuario?.descripcion ?: stringResource(R.string.sin_descripcion),
                    isEditing = uiState.isEditingDescripcion,
                    onEdit = { viewModel.startEditingDescripcion() },
                    onSave = { newValue ->
                        viewModel.updateDescripcion(newValue)
                    },
                    onCancel = { viewModel.cancelEditing() },
                    multiline = true
                )

                EditablePasswordContent(
                    isEditing = uiState.isEditingPassword,
                    onEdit = { viewModel.startEditingPassword() },
                    onSave = { newPassword ->
                        viewModel.updatePassword(newPassword)
                    },
                    onCancel = { viewModel.cancelEditing() }
                )
            }
        }
    }
}

@Composable
fun EditableInfoContent(
    titulo: String,
    contenido: String,
    isEditing: Boolean,
    onEdit: () -> Unit,
    onSave: (String) -> Unit,
    onCancel: () -> Unit,
    multiline: Boolean = false
) {
    var textValue by remember(contenido, isEditing) {
        mutableStateOf(contenido)
    }

    LaunchedEffect(contenido) {
        if (!isEditing) {
            textValue = contenido
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 17.sp
            )
            if (!isEditing) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = stringResource(R.string.editar)
                    )
                }
            }
        }

        if (isEditing) {
            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = !multiline,
                maxLines = if (multiline) 5 else 1
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    textValue = contenido
                    onCancel()
                }) {
                    Text(stringResource(R.string.cancelar))
                }
                TextButton(
                    onClick = {
                        onSave(textValue)
                    },
                    enabled = textValue.isNotBlank() && textValue != contenido
                ) {
                    Text(stringResource(R.string.guardar))
                }
            }
        } else {
            Text(
                text = contenido.ifBlank { stringResource(R.string.sin_descripcion) },
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 17.sp,
                color = if (contenido.isBlank())
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun EditablePasswordContent(
    isEditing: Boolean,
    onEdit: () -> Unit,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.password),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 17.sp
            )
            if (!isEditing) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = stringResource(R.string.editar)
                    )
                }
            }
        }

        if (isEditing) {
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.nueva_contrasena)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.confirmar_contrasena)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    newPassword = ""
                    confirmPassword = ""
                    onCancel()
                }) {
                    Text(stringResource(R.string.cancelar))
                }
                TextButton(
                    onClick = {
                        if (newPassword == confirmPassword && newPassword.length >= 6) {
                            onSave(newPassword)
                            newPassword = ""
                            confirmPassword = ""
                        }
                    },
                    enabled = newPassword == confirmPassword && newPassword.length >= 6
                ) {
                    Text(stringResource(R.string.guardar))
                }
            }
        } else {
            Text(
                text = "••••••••",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 17.sp,
            )
        }
    }
}