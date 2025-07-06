package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
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
import com.example.shift_e.ui.components.SuccessDialog
import com.example.shift_e.ui.theme.BlackLight
import com.example.shift_e.ui.theme.CreamBackground
import com.example.shift_e.ui.theme.TealDark

@Composable
fun SignUpOtpScreen(navController: NavController, email: String) {
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

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

        // 2) Overlay title
        Text(
            text = "Sign Up",
            color = Color.White,
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .offset(x = 20.dp, y = 195.dp)
        )

        // 3) Cream card with rounded top corners
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 260.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(CreamBackground)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Instruction text
            Text(
                text = "We’ve sent a code to your email\n($email). Please enter it below to confirm your account.",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                lineHeight = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
            )

            // OTP field
            PillTextField(
                value = otp,
                onValueChange = { otp = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter OTP",
                isPassword = false
            )
            Spacer(Modifier.height(40.dp))

            Text(
                text = "Password",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Password field
            PillTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter password",
                isPassword = true
            )
            Spacer(Modifier.height(20.dp))

            Text(
                text = "Re-Enter Password",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Re‑enter password field
            PillTextField(
                value = rePassword,
                onValueChange = { rePassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Re‑enter password",
                isPassword = true
            )
            Spacer(Modifier.height(45.dp))

            // Create Account button (dark‑teal)
            PillButton(
                text = "CREATE ACCOUNT",
                onClick = {
                    when {
                        otp.isBlank() || password.isBlank() || rePassword.isBlank() ->
                            showToast = "Please fill all fields"
                        otp != "1234" ->
                            showToast = "Invalid OTP"
                        password != rePassword ->
                            showToast = "Passwords do not match"
                        else -> {
                            showSuccess = true        // <-- Show the pop‑up instead of navController.navigate()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            Spacer(Modifier.height(20.dp))

            // Cancel button (outlined)
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TealDark,
                    containerColor = CreamBackground
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Text("CANCEL", fontSize = 14.sp)
            }
        }

        // Toast messages
        showToast?.let {
            ShowToast(it)
            showToast = null
        }

        if (showSuccess) {
            SuccessDialog(
                message = "Account created successfully!\nLet’s finish your profile.",
                onButtonClick = {
                    showSuccess = false
                    navController.navigate("profile")
                },
                onDismiss = {
                    showSuccess = false
                }
            )
        }
    }
}
