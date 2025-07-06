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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.PillButton
import com.example.shift_e.ui.components.PillTextField
import com.example.shift_e.ui.components.ShowToast
import com.example.shift_e.ui.theme.BlackLight
import com.example.shift_e.ui.theme.CreamBackground
import com.example.shift_e.ui.theme.TealDark

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) }

    Box(Modifier.fillMaxSize()) {
        // 1) Header image
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Text(
                text = "Sign In",
                color = Color.White,
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y=195.dp, x = 20.dp)
            )

        // 2) Cream card with rounded top corners
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = 260.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(CreamBackground)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Username",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // 4a) Username field
            PillTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter your username",
                isPassword = false
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Password",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // 4b) Password field
            PillTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter your password",
                isPassword = true
            )
            Spacer(Modifier.height(38.dp))

            // 5) LOGIN button
            PillButton(
                text = "LOGIN",
                onClick = {
                    when {
                        username.isBlank() || password.isBlank() ->
                            showToast = "Please enter username and password"
                        username != "user" || password != "password123" ->
                            showToast = "Invalid credentials"
                        else -> {
                            // pass username through the route
                            navController.navigate("dashboard?username=$username") {
                                // clear backstack so “Back” exits the app
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            )

            Spacer(Modifier.height(20.dp))

            // 6) Google button
            OutlinedButton(
                onClick = { showToast = "Feature under implementation" },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Gray,
                    containerColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Sign in with Google", fontSize = 14.sp)
            }
            Spacer(Modifier.height(20.dp))

            // 7) Forgot password
            Text(
                "Forgot password?",
                color = TealDark,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { navController.navigate("forgot_password") }
            )
            Spacer(Modifier.height(100.dp))

            // 8) Footer
            Row(horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", color = Color.Gray)
                Text(
                    "Sign up",
                    color = TealDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("signup") }
                )
            }
        }

        // 9) Show toast if needed
        showToast?.let {
            ShowToast(it)
            showToast = null
        }
    }
}
