package com.kriscg.belek.ui.screens.encuestas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.R

val tipos = listOf(
    "Arqueológicos",
    "Naturales",
    "Históricos",
    "Culturales",
    "Ecoturísticos",
    "Gastronómicos",
    "Recreativos",
    "Comerciales"
)

val presupuesto = listOf("Alto", "Mediano", "Bajo")
val intereses = listOf("Gastronomía", "Fotografía", "Artesanías y compras", "Aventura", "Relajación")

@Composable
fun EncuestaScreen(
    modifier: Modifier = Modifier
){
    val destino = remember { mutableStateOf("") }
    val tipoSeleccionado = remember { mutableStateOf("") }
    val presupuestoSeleccionado = remember { mutableStateOf("") }
    val interesesSeleccionados = remember { mutableStateOf(setOf<String>()) }

    Box(
        modifier = modifier.fillMaxSize()
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ){
            Text(
                text = "Nuevo Viaje",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Destino de viaje:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            TextField(
                value = destino.value,
                onValueChange = { destino.value = it },
                shape = RoundedCornerShape(10.dp),
                placeholder = { Text("Dirección") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.destino),
                contentDescription = "Destino",
                contentScale = ContentScale.Fit,
                alpha = 0.3f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = "Tipo de viaje:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tipos) { tipo ->
                    FiltroBoton(
                        texto = tipo,
                        seleccionado = tipoSeleccionado.value == tipo,
                        onClick = {
                            tipoSeleccionado.value = if (tipoSeleccionado.value == tipo) "" else tipo
                        }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Presupuesto:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(presupuesto) { pres ->
                    FiltroBoton(
                        texto = pres,
                        seleccionado = presupuestoSeleccionado.value == pres,
                        onClick = {
                            presupuestoSeleccionado.value = if (presupuestoSeleccionado.value == pres) "" else pres
                        }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Intereses específicos:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(intereses) { interes ->
                    FiltroBoton(
                        texto = interes,
                        seleccionado = interesesSeleccionados.value.contains(interes),
                        onClick = {
                            val nuevosIntereses = interesesSeleccionados.value.toMutableSet()
                            if (nuevosIntereses.contains(interes)) {
                                nuevosIntereses.remove(interes)
                            } else {
                                nuevosIntereses.add(interes)
                            }
                            interesesSeleccionados.value = nuevosIntereses
                        }
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6650a4)
                )
            ) {
                Text(
                    text = "Ver opciones",
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun FiltroBoton(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado) Color(0xFF2D5F4D) else Color(0xFFE8E8E8),
            contentColor = if (seleccionado) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = texto,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EncuestaPreview() {
    EncuestaScreen(
        modifier = Modifier.fillMaxSize()
    )
}