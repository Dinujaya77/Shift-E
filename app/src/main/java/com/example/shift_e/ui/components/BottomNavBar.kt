package com.example.shift_e.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

private data class NavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val navItems = listOf(
    NavItem("dashboard", Icons.Default.Home),
    NavItem("trips",     Icons.Default.History),
    NavItem("payment",   Icons.Default.Payment),
    NavItem("profile",   Icons.Default.Person)
)

@Composable
fun BottomNavBar(navController: NavController) {
    // Observe current route
    val backStack = navController.currentBackStackEntryAsState().value
    val currentRoute = backStack?.destination?.route

    Box(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.secondary) // TealDark in your theme
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val selected = currentRoute?.startsWith(item.route) == true
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.route,
                    tint = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            // avoid multiple copies on stack
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                )
            }
        }
    }
}
