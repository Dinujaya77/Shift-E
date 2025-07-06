package com.example.shift_e.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shift_e.ui.screens.DashboardScreen
import com.example.shift_e.ui.screens.LoginScreen
import com.example.shift_e.ui.screens.ProfileCreationScreen
import com.example.shift_e.ui.screens.SignUpOtpScreen
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
        composable(
            route = "signup_otp?email={email}",
            arguments = listOf(navArgument("email") {
                defaultValue = ""
            })
        ) { backStack ->
            val email = backStack.arguments?.getString("email") ?: ""
            SignUpOtpScreen(navController, email)
        }
//         4) Profile creation (after OTP)
        composable("profile") {
            ProfileCreationScreen(navController)
        }
//        // 5) Dashboard
        composable(
        route = "dashboard?username={username}",
        arguments = listOf(navArgument("username") {
            defaultValue = "user"
        })
    ) { backStack ->
        val user = backStack.arguments?.getString("username") ?: "user"
        DashboardScreen(navController, username = user)
    }
    }
}
