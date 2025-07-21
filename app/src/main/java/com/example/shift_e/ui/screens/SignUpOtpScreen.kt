package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.shift_e.ui.components.SuccessDialog
import com.example.shift_e.ui.theme.BlackLight
import com.example.shift_e.ui.theme.CreamBackground
import com.example.shift_e.ui.theme.TealDark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpOtpScreen(navController: NavController, email: String) {
    val focusManager = LocalFocusManager.current
    val passwordRequester = remember { FocusRequester() }
    val repassRequester = remember { FocusRequester() }

    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

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
                text = "We’ve sent a code to your email\n($email). Please enter it below to confirm your account.",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                lineHeight = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
            )

            PillTextField(
                value = otp,
                onValueChange = { otp = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter OTP",
                isPassword = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { passwordRequester.requestFocus() }
                )
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

            PillTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordRequester),
                placeholder = "Enter password",
                isPassword = !passwordVisible,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { repassRequester.requestFocus() }
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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

            PillTextField(
                value = rePassword,
                onValueChange = { rePassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(repassRequester),
                placeholder = "Re‑enter password",
                isPassword = !passwordVisible,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        handleCreateAccount(
                            email = email,
                            password = password,
                            rePassword = rePassword,
                            otp = otp,
                            auth = auth,
                            db = db,
                            showToastSetter = { showToast = it },
                            showSuccessSetter = { showSuccess = it }
                        )
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
            Spacer(Modifier.height(45.dp))

            PillButton(
                text = "CREATE ACCOUNT",
                onClick = {
                    focusManager.clearFocus()
                    handleCreateAccount(
                        email = email,
                        password = password,
                        rePassword = rePassword,
                        otp = otp,
                        auth = auth,
                        db = db,
                        showToastSetter = { showToast = it },
                        showSuccessSetter = { showSuccess = it }
                    )
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            )
            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
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

        showToast?.let {
            ShowToast(it)
            showToast = null
        }

        if (showSuccess) {
            SuccessDialog(
                message = "Account created successfully!\nLet’s finish your profile.",
                onButtonClick = {
                    showSuccess = false
                    navController.navigate("profilecreation")
                },
                onDismiss = { showSuccess = false }
            )
        }
    }
}

// ✅ Helper method for account creation logic
private fun handleCreateAccount(
    email: String,
    password: String,
    rePassword: String,
    otp: String,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    showToastSetter: (String?) -> Unit,
    showSuccessSetter: (Boolean) -> Unit
) {
    when {
        otp.isBlank() || password.isBlank() || rePassword.isBlank() -> {
            showToastSetter("Please fill all fields")
        }

        otp != "1234" -> {
            showToastSetter("Invalid OTP")
        }

        password != rePassword -> {
            showToastSetter("Passwords do not match")
        }

        else -> {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                        val user = hashMapOf("email" to email, "uid" to uid)
                        db.collection("users").document(uid).set(user)
                            .addOnSuccessListener {
                                showSuccessSetter(true)
                            }
                            .addOnFailureListener {
                                showToastSetter("Failed to save user to Firestore")
                            }
                    } else {
                        showToastSetter(task.exception?.message ?: "Failed to create account")
                    }
                }
        }
    }
}
