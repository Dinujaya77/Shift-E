package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) }
    val passwordRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    fun loginUser() {
        val trimmedEmail = email.trim().lowercase()
        if (trimmedEmail.isBlank() || password.isBlank()) {
            showToast = "Please enter email and password"
            return
        }

        auth.signInWithEmailAndPassword(trimmedEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val displayName = trimmedEmail.substringBefore("@")
                    navController.navigate("dashboard?username=$displayName") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    showToast = task.exception?.message ?: "Login failed"
                }
            }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )

        Text(
            text = "Sign In",
            color = Color.White,
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.offset(y = 195.dp, x = 20.dp)
        )

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
                "Email",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                     .fillMaxWidth()
                    .padding(bottom = 12.dp, start = 5.dp)
            )
            PillTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter your email",
                isPassword = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordRequester.requestFocus() })
            )
            Spacer(Modifier.height(16.dp))

            Text(
                "Password",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, start = 5.dp)
            )
            PillTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth().focusRequester(passwordRequester),
                placeholder = "Enter your password",
                isPassword = !passwordVisible,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    loginUser()
                }),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
            Spacer(Modifier.height(38.dp))

            PillButton(
                text = "LOGIN",
                onClick = {
                    focusManager.clearFocus()
                    loginUser()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            )
            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = { showToast = "Feature under implementation" },
                modifier = Modifier.fillMaxWidth().height(48.dp),
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

            Text(
                "Forgot password?",
                color = TealDark,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { showToast = "Feature under implementation" }
            )
            Spacer(Modifier.height(100.dp))

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

        showToast?.let {
            ShowToast(it)
            showToast = null
        }
    }
}
