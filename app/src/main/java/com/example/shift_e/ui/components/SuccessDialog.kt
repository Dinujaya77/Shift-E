package com.example.shift_e.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shift_e.R
import com.example.shift_e.ui.theme.CreamBackground
import com.example.shift_e.ui.theme.TealDark

@Composable
fun SuccessDialog(
    message: String,
    buttonText: String = "TAKE ME TO PROFILE",
    onButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    // Full‑screen translucent backdrop
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0x88000000)) // semi‑transparent black
    ) {
        // Centered card
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CreamBackground)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Optional success icon
//            Icon(
//                painter = painterResource(id = R.drawable.ic_success), // your confetti / check icon
//                contentDescription = null,
//                tint = TealDark,
//                modifier = Modifier.size(64.dp)
//            )
//            Spacer(Modifier.height(16.dp))

            Text(
                text = "Awesome!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = TealDark
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(24.dp))

            // Primary button
            Button(
                onClick = onButtonClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealDark,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(buttonText)
            }
            Spacer(Modifier.height(12.dp))

            // Secondary “Cancel/Dismiss”
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TealDark,
                    containerColor = CreamBackground
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("CANCEL")
            }
        }
    }
}
