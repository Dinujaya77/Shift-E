package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.PillButton
import com.example.shift_e.ui.components.PillTextField
import com.example.shift_e.ui.components.ShowToast
import com.example.shift_e.ui.theme.CreamBackground
import com.example.shift_e.ui.theme.BlackLight
import com.example.shift_e.ui.theme.TealDark

@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) }

    fun validateAndProceed() {
        val trimmedEmail = email.trim().lowercase()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            showToast = "Please enter a valid email"
        } else {
            navController.navigate("signup_otp?email=$trimmedEmail")
        }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Text(
            text = "Sign Up",
            color = Color.White,
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.offset(x = 20.dp, y = 195.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 260.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(CreamBackground)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Email",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            PillTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter your email",
                isPassword = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { validateAndProceed() }
                )
            )
            Spacer(Modifier.height(50.dp))

            PillButton(
                text = "NEXT",
                onClick = { validateAndProceed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = { showToast = "Feature under implementation" },
                enabled = false,
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
                Image(
                    painter = painterResource(R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Sign up with Google", fontSize = 14.sp)
            }

            Spacer(Modifier.height(150.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Text("Already have an account? ", color = Color.Gray)
                Text(
                    "Sign in",
                    color = TealDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
        }

        showToast?.let {
            ShowToast(it)
            showToast = null
        }
    }
}
