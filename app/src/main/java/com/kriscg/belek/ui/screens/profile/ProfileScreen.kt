package com.kriscg.belek.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kriscg.belek.R
import com.kriscg.belek.ui.viewModel.MainProfileViewModel
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.domain.Resena
import com.kriscg.belek.util.TranslationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onLugarClick: (Int) -> Unit = {},
    viewModel: MainProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val preferencesManager = remember {
        com.kriscg.belek.data.userpreferences.PreferencesManager.getInstance(context)
    }
    val preferences by preferencesManager.preferencesFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.perfil),
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
                },
                actions = {
                    IconButton(onClick = onEditProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.editar_perfil)
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = stringResource(R.string.perfil),
                        modifier = Modifier.size(125.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = uiState.usuario?.username ?: stringResource(R.string.nombre_usuario),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.usuario?.descripcion
                                ?: stringResource(R.string.sin_descripcion),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(0.90F)
                        .padding(10.dp)
                )

                SingleChoiceSegmentedButtonRow {
                    SegmentedButton(
                        selected = uiState.selectedTabIndex == 0,
                        onClick = { viewModel.onTabSelected(0) },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                        icon = {
                            SegmentedButtonDefaults.Icon(active = uiState.selectedTabIndex == 0) {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                )
                            }
                        }
                    ) {
                        Text(stringResource(R.string.favoritos))
                    }

                    SegmentedButton(
                        selected = uiState.selectedTabIndex == 1,
                        onClick = { viewModel.onTabSelected(1) },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                        icon = {
                            SegmentedButtonDefaults.Icon(active = uiState.selectedTabIndex == 1) {
                                Icon(
                                    Icons.Default.Bookmark,
                                    contentDescription = null,
                                    modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                )
                            }
                        }
                    ) {
                        Text(stringResource(R.string.guardados))
                    }

                    SegmentedButton(
                        selected = uiState.selectedTabIndex == 2,
                        onClick = { viewModel.onTabSelected(2) },
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                        icon = {
                            SegmentedButtonDefaults.Icon(active = uiState.selectedTabIndex == 2) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                )
                            }
                        }
                    ) {
                        Text(stringResource(R.string.resenas))
                    }
                }

                when (uiState.selectedTabIndex) {
                    0 -> FavoritosContent(
                        lugares = uiState.favoritos,
                        onLugarClick = onLugarClick,
                        currentLanguage = preferences.language
                    )
                    1 -> GuardadosContent(
                        lugares = uiState.guardados,
                        onLugarClick = onLugarClick,
                        currentLanguage = preferences.language
                    )
                    2 -> ResenasContent(
                        resenas = uiState.resenas,
                        onLugarClick = onLugarClick,
                        currentLanguage = preferences.language
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritosContent(
    lugares: List<Lugar>,
    onLugarClick: (Int) -> Unit,
    currentLanguage: String
) {
    if (lugares.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_favoritos),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        ContenidoLista(
            lugares = lugares,
            bordeColor = MaterialTheme.colorScheme.primary,
            onLugarClick = onLugarClick,
            currentLanguage = currentLanguage,
            showDeleteButton = false
        )
    }
}

@Composable
fun GuardadosContent(
    lugares: List<Lugar>,
    onLugarClick: (Int) -> Unit,
    currentLanguage: String
) {
    if (lugares.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_guardados),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        ContenidoLista(
            lugares = lugares,
            bordeColor = MaterialTheme.colorScheme.primary,
            onLugarClick = onLugarClick,
            currentLanguage = currentLanguage,
            showDeleteButton = false
        )
    }
}

@Composable
fun ResenasContent(
    resenas: List<Pair<Resena, Lugar?>>,
    onLugarClick: (Int) -> Unit,
    currentLanguage: String
) {
    if (resenas.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_resenas_usuario),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(resenas) { (resena, lugar) ->
                    Column(
                        modifier = Modifier.clickable {
                            lugar?.id?.let { onLugarClick(it) }
                        }
                    ) {
                        Text(
                            text = lugar?.let { TranslationHelper.getLugarNombre(it, currentLanguage) }
                                ?: stringResource(R.string.lugar_desconocido),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = resena.comentario,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(modifier = Modifier.fillMaxWidth()
                            .padding(end = 4.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star ${index + 1}",
                                    tint = if (index < resena.calificacion)
                                        MaterialTheme.colorScheme.secondaryContainer
                                    else
                                        MaterialTheme.colorScheme.tertiaryContainer,
                                    modifier = Modifier.size(20.dp)
                                        .padding(end = 4.dp)
                                )
                            }
                        }
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
fun ContenidoLista(
    lugares: List<Lugar>,
    bordeColor: Color,
    onLugarClick: (Int) -> Unit,
    currentLanguage: String,
    showDeleteButton: Boolean = false,
    onRemove: ((Int) -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(0.9f)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, bordeColor),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(lugares) { lugar ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { lugar.id?.let { onLugarClick(it) } }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = TranslationHelper.getLugarNombre(lugar, currentLanguage),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = TranslationHelper.getLugarDescripcion(lugar, currentLanguage),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (lugar.imagenUrl != null) {
                        AsyncImage(
                            model = lugar.imagenUrl,
                            contentDescription = lugar.nombre,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(30.dp)),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.tikal_prueba)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.tikal_prueba),
                            contentDescription = lugar.nombre,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(30.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    if (showDeleteButton && onRemove != null) {
                        IconButton(onClick = { lugar.id?.let { onRemove(it) } }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.eliminar),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}