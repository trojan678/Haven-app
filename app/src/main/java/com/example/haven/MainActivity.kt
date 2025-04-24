package com.example.haven

import Loginscreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.haven.ui.theme.HavenTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HavenTheme {
                HavenRoot()
            }
        }
    }
}

@Composable
fun HavenRoot() {
    var loggedIn by remember { mutableStateOf(false) }

    if (loggedIn) {
        Text("Welcome to Haven Home Screen!") // replace with HomeScreen later
    } else {
        Loginscreen(
            onLoginSuccess = { loggedIn = true },
            onRegisterClick = { /* navigate to register screen */ }
        )
    }
}


