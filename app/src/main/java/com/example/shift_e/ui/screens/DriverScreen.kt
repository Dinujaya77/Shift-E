package com.example.shift_e.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shift_e.ui.components.BottomNavBar
import com.example.shift_e.ui.theme.TealDark
import java.sql.Driver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverScreen(navController: NavController, username: String = "user") {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Previous Trips", style = MaterialTheme.typography.headlineMedium)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Driver Screen") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TealDark,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar= { BottomNavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text("• Ride History (to be implemented)")
            Text("• Promotions (to be implemented)")
            Text("• Recent Activity (to be implemented)")
        }
    }
}
