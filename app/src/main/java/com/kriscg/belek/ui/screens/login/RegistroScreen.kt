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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kriscg.belek.R


@Composable
fun RegistroScreen(
    onRegistroClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
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
                value = email.value,
                onValueChange = { email.value = it },
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
                onClick = onRegistroClick,
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