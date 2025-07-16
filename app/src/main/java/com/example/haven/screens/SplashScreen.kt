package com.example.haven.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.haven.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {

    LaunchedEffect(Unit) {
        delay(3000)
        onSplashComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "splash",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Content on top
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Your safe space",
                fontSize = 25.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            )
        }
    }
}