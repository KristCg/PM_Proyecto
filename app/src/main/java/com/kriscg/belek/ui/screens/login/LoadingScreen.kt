package com.kriscg.belek.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.kriscg.belek.R


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
){
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
                text = "B’ena Tour",
                fontSize = 40.sp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Sabias que: Guatemala se conoce como el pais de la eterna primavera",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(40.dp))

            CircularProgressIndicator()



        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoadingScreen() {
    LoadingScreen()
}