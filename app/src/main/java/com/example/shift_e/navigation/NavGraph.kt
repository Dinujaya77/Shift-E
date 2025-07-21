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
            "signup_otp?email={email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStack ->
            val email = backStack.arguments?.getString("email") ?: ""
            SignUpOtpScreen(navController, email)
        }
        composable("profilecreation") {
            ProfileCreationScreen(navController)
        }
        composable("trips") {
            TripsScreen(navController)
        }
        composable("payment") {
            PaymentScreen(navController)
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
