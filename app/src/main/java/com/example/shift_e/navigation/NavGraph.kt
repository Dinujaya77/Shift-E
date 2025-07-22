package com.example.shift_e.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shift_e.ui.screens.*
import com.example.shift_e.ui.screens.dashboard.DashboardScreen
import com.example.shift_e.ui.screens.profile.ProfileScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String = "login") {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable(
            route = "payment?origin={origin}",
            arguments = listOf(navArgument("origin") { defaultValue = "nsbm" })
        ) { backStackEntry ->
            val origin = backStackEntry.arguments?.getString("origin") ?: "nsbm"
            PaymentScreen(navController, origin)
        }
        composable("profilecreation") {
            ProfileCreationScreen(navController)
        }
        composable("trips") {
            TripsScreen(navController)
        }
        composable(
            "payment?origin={origin}",
            arguments = listOf(navArgument("origin") { defaultValue = "NSBM" })
        ) { backStackEntry ->
            val origin = backStackEntry.arguments?.getString("origin") ?: "NSBM"
            PaymentScreen(navController, origin)
        }
        composable("qrscanner") {
            QrScannerScreen(navController)
        }
        composable("driverscreen") {
            DriverScreen(navController)
        }

        // ✅ Use ViewModel for username internally
        composable("dashboard") {
            DashboardScreen(navController)
        }

        // ✅ Same for profile screen
        composable("profile") {
            ProfileScreen(navController)
        }
    }
}
