package com.example.shift_e.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.BottomNavBar
import com.example.shift_e.ui.components.WalletCardImage
import com.example.shift_e.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3C5E45), Color(0xFF6D9F7B), Color(0xFF1B1B1B))
    )

    val user by userViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadUserData()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(brush = backgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = { BottomNavBar(navController) }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text("Profile Page", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(120.dp).clip(CircleShape).shadow(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(user.firstName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(user.email, fontSize = 18.sp, color = Color.White)
                Text(user.mobile, fontSize = 18.sp, color = Color.White)

                Spacer(modifier = Modifier.height(28.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("User Statistics", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text("Total Rides completed: ${user.totalRides}", color = Color.White, fontSize = 16.sp)
                        Text("Money Saved: LKR${user.moneySaved}", color = Color.White, fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("User Rating: ${user.rating}", color = Color.White, fontSize = 16.sp)
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = Color.Yellow,
                                modifier = Modifier.size(22.dp).padding(start = 6.dp)
                            )
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("Wallet", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 20.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth().height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            WalletCardImage(resId = R.drawable.card_1, modifier = Modifier.offset(x = (-50).dp, y = 20.dp).size(180.dp, 110.dp))
                            WalletCardImage(resId = R.drawable.card_2, modifier = Modifier.offset(x = 30.dp, y = (-20).dp).size(200.dp, 120.dp).shadow(12.dp, RoundedCornerShape(20.dp)))
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Selected Payment Method", fontSize = 15.sp, color = Color.White.copy(alpha = 0.95f), modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
