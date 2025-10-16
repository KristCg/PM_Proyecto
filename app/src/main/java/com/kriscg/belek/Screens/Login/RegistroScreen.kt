package com.kriscg.belek.Screens.Login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.R.attr.logo
import com.kriscg.belek.ui.theme.BelekTheme
import kotlinx.serialization.Serializable
import com.kriscg.belek.R
import com.kriscg.belek.ui.theme.Purple40


@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier
){
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(
        modifier = modifier.fillMaxSize()
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                alpha = 0.3f
            )

            Text(
                text = "Registro",
                fontSize = 40.sp
            )

            Spacer(Modifier.height(32.dp))

            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text("Usuario") },
                singleLine = true
            )
            Spacer(Modifier.height(20.dp))

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text("Correo") },
                singleLine = true
            )

            Spacer(Modifier.height(20.dp))

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text("Contraseña") },
                singleLine = true
            )

            Spacer(Modifier.height(20.dp))


            Button(
                onClick = { },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6650a4)
                )
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 15.sp
                )
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
private fun RegistroPreview() {
    RegistroScreen(
        modifier = Modifier.fillMaxSize()
    )
}