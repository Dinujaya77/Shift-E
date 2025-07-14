package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.BottomNavBar
import com.example.shift_e.ui.theme.TealDark
import java.sql.Driver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverScreen(navController: NavController, username: String = "user") {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3C5E45),
            Color(0xFF6D9F7B),
            Color(0xFF1B1B1B)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = { BottomNavBar(navController) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Top Bar with Title and Profile Image
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trip Information",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_profile),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate("profile/$username")
                            }
                    )
                }

                // Trip Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("🏁 90km", color = Color.White, fontSize = 18.sp)
                        Text("DUR 2 mins", color = Color.White, fontSize = 14.sp)
                        Text("Dist 500 m", color = Color.White, fontSize = 14.sp)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "10",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text("KMPH", color = Color.White, fontSize = 16.sp)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Direction",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Text("500 m", color = Color.White, fontSize = 14.sp)
                    }
                }

                // Cost Info
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color(0xFF1B3C1A), RoundedCornerShape(20.dp))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cost: LKR 50",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Warning Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF520000)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Please be Informed",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "After exceeding the limit of the\nschool junction drop off point cost\nwill be added with respect to\nDistance and Time.",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Payment Method Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2E28)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("Payment Method", color = Color.White, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(12.dp))

                        Image(
                            painter = painterResource(id = R.drawable.card_2),
                            contentDescription = "Visa Card",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

