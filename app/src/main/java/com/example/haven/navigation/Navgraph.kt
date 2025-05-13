package com.example.haven.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.haven.screens.Loginscreen
import com.example.haven.screens.Homescreen
import com.example.haven.screens.Registerscreen
import com.example.haven.screens.MessageReceiptScreen
import androidx.navigation.navArgument
import androidx.navigation.NavType



@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            Loginscreen(
                onLoginSuccess = {
                    navController.navigate("home"){
                        popUpTo("login") { inclusive = true }
                    } },
                onRegisterClick = {
                    navController.navigate("register") }
            )
        }

        composable("register") {
            Registerscreen(
                onRegisterSuccess = { navController.navigate("home") },
                onLoginClick = { navController.popBackStack ()}
            )
        }

        composable("home") {
            Homescreen(navController= navController)
        }
        composable (
            route = "receipt/{parlorName}/{massageType}/{priceRange}",
            arguments = listOf(
                navArgument("parlorName") { type = NavType.StringType },
                navArgument("massageType") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType }
            )
        ) { backStackEntry ->
                MessageReceiptScreen(
                    parlorName = backStackEntry.arguments?.getString("parlorName") ?: "",
                    massageType = backStackEntry.arguments?.getString("massageType") ?: "",
                    price = backStackEntry.arguments?.getString("price") ?: "",
                    navController = navController
                ) { navController.popBackStack() }
        }
    }
}
