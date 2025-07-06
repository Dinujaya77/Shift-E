package com.example.shift_e.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shift_e.ui.screens.LoginScreen
import com.example.shift_e.ui.screens.SignUpScreen
//import com.example.shift_e.ui.screens.SignUpOtpScreen

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
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
//            SignUpOtpScreen(navController, email)
        }
        // Add other screens here as you implement them
    }
}
