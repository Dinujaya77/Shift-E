package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.PillButton
import com.example.shift_e.ui.components.PillTextField
import com.example.shift_e.ui.components.ShowToast

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header image
        Image(
            painter = painterResource(id = R.drawable.login_background), // TODO: Replace with your asset
            contentDescription = "Header Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .align(Alignment.TopCenter)
        )

        // Main Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 180.dp, start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            PillTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            PillTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            PillButton(
                text = "LOGIN",
                onClick = {
                    when {
                        username.isBlank() || password.isBlank() -> showToast = "Please enter username and password"
                        username != "user" || password != "password123" -> showToast = "Invalid credentials"
                        else -> navController.navigate("dashboard") // TODO: Replace with real navigation
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .height(48.dp)
            )
            // Google Sign-In (disabled)
            OutlinedButton(
                onClick = { showToast = "Feature under implementation" },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google), // TODO: Replace with Google icon asset
                    contentDescription = "Google",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Google")
            }
            // Forgot password
            Text(
                text = "Forgot password?",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
                    .clickable { navController.navigate("forgot_password") },
                fontSize = 14.sp
            )
            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ")
                Text(
                    text = "Sign up",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("signup") }
                )
            }
        }
        // Show Toast
        showToast?.let {
            ShowToast(it)
            showToast = null
        }
    }
}
