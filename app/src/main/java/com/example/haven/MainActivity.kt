package com.example.haven

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.haven.screens.Homescreen
import com.example.haven.screens.Loginscreen
import com.example.haven.screens.MessageReceiptScreen
import com.example.haven.screens.ProfileScreen
import com.example.haven.screens.Registerscreen
import com.example.haven.ui.theme.HavenTheme
import com.google.firebase.FirebaseApp
import java.net.URLDecoder


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            HavenTheme {
                val navController = rememberNavController()
                HavenApp(navController = navController)
            }
        }
    }
}

@Composable
fun HavenApp(navController: NavController) {
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
            Homescreen(navController = navController)
        }
        composable(
            route = "receipt/{parlorName}/{massageType}/{price}",
            arguments = listOf(
                navArgument("parlorName") { type = NavType.StringType },
                navArgument("massageType") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType }
            )
            ) { backStackEntry ->
            val parlorName = backStackEntry.arguments?.getString("parlorName") ?: ""
            val massageType = backStackEntry.arguments?.getString("massageType") ?: ""
            val price = backStackEntry.arguments?.getString("price") ?: ""

            MessageReceiptScreen(
                parlorName = URLDecoder.decode(parlorName, "UTF-8"),
                massageType = URLDecoder.decode(massageType, "UTF-8"),
                price = price,
                navController = navController
            ){ navController.popBackStack() }
        }
        composable ("profile_screen") {
            ProfileScreen(navController = navController)
        }
    }
}