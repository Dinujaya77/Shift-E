package com.example.shift_e.ui.screens

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.BottomNavBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, username: String = "User") {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3C5E45),
            Color(0xFF6D9F7B),
            Color(0xFF1B1B1B)
        )
    )

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid

    var email by remember { mutableStateOf("Loading...") }
    var mobile by remember { mutableStateOf("Loading...") }
    var firstName by remember { mutableStateOf(username) }

    // User statistics
    var totalRides by remember { mutableStateOf(0) }
    var moneySaved by remember { mutableStateOf(0) }
    var rating by remember { mutableStateOf(0.0) }

    // Fetch user info and statistics from Firestore
    LaunchedEffect(uid) {
        uid?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { doc ->
                    doc.getString("firstName")?.let { firstName = it }
                    doc.getString("email")?.let { email = it }
                    doc.getString("mobile")?.let { mobile = it }

                    doc.getLong("totalRides")?.let { totalRides = it.toInt() }
                    doc.getLong("moneySaved")?.let { moneySaved = it.toInt() }
                    doc.getDouble("rating")?.let { rating = it }
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Profile Page",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .shadow(10.dp, shape = CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(firstName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(email, fontSize = 18.sp, color = Color.White)
                Text(mobile, fontSize = 18.sp, color = Color.White)

                Spacer(modifier = Modifier.height(28.dp))

                // Stats Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("User Statistics", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text("Total Rides completed: $totalRides", color = Color.White, fontSize = 16.sp)
                        Text("Money Saved: LKR$moneySaved", color = Color.White, fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("User Rating: $rating", color = Color.White, fontSize = 16.sp)
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = Color.Yellow,
                                modifier = Modifier.size(22.dp).padding(start = 6.dp)
                            )
                        }
                    }
                }

                // Wallet Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Wallet",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            WalletCardImage(
                                resId = R.drawable.card_1,
                                modifier = Modifier
                                    .offset(x = (-50).dp, y = 20.dp)
                                    .size(width = 180.dp, height = 110.dp)
                            )

                            WalletCardImage(
                                resId = R.drawable.card_2,
                                modifier = Modifier
                                    .offset(x = 30.dp, y = (-20).dp)
                                    .size(width = 200.dp, height = 120.dp)
                                    .shadow(12.dp, RoundedCornerShape(20.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Selected Payment Method",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.95f),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun WalletCardImage(resId: Int, modifier: Modifier = Modifier) {
    val painter: Painter = painterResource(id = resId)
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "Card Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
