package com.example.shift_e.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shift_e.ui.theme.TealDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, username: String = "user") {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TealDark,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Welcome, $username!",
                style = MaterialTheme.typography.headlineMedium,
                color = TealDark
            )
            Spacer(Modifier.height(16.dp))
            Text("• Ride History (to be implemented)")
            Text("• Promotions (to be implemented)")
            Text("• Recent Activity (to be implemented)")
        }
    }
}
