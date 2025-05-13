package com.example.haven

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.haven.screens.Loginscreen
import com.example.haven.screens.Registerscreen
import com.example.haven.screens.Homescreen
import com.example.haven.ui.theme.HavenTheme
import com.google.firebase.FirebaseApp
import androidx.compose.material3.Text


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            HavenTheme {
                HavenApp()
            }
        }
    }
}

@Composable
fun HavenApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            Loginscreen(
                onLoginSuccess = { navController.navigate("home") },
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            Registerscreen(
                onRegisterSuccess = { navController.navigate("home") },
                onLoginClick = { navController.popBackStack() }
            )
        }
        composable("home") {
            Homescreen()
        }
    }
}

@Composable
fun Homescreen() {
    Text("Haven Home")
}