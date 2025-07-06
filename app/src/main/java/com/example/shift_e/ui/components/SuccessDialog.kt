package com.example.shift_e.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shift_e.ui.theme.CreamBackground
import com.example.shift_e.ui.theme.TealDark

@Composable
fun SuccessDialog(
    message: String,
    buttonText: String = "TAKE ME TO PROFILE",
    onButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0x88000000))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CreamBackground)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = TealDark,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Awesome!",
                style = MaterialTheme.typography.headlineMedium,
                color = TealDark
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(24.dp))
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
