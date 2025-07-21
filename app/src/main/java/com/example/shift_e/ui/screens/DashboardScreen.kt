package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.BottomNavBar
import com.example.shift_e.ui.theme.TealDark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.unit.Dp

data class LocationData(val id: String, val name: String, val ridesAvailable: Int)
data class RideActivity(val location: String, val date: String, val time: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, username: String = "User") {
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var locations by remember { mutableStateOf<List<LocationData>>(emptyList()) }
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }
    var activities by remember { mutableStateOf<List<RideActivity>>(emptyList()) }

    // Load locations
    LaunchedEffect(Unit) {
        db.collection("locations").get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    val name = doc.getString("name")
                    val rides = doc.getLong("ridesAvailable")?.toInt()
                    if (name != null && rides != null) LocationData(doc.id, name, rides) else null
                }
                locations = list
                if (selectedLocation == null && list.isNotEmpty()) selectedLocation = list[0]
            }
    }

    // Load activity list
    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("users").document(uid)
                .collection("activities")
                .get()
                .addOnSuccessListener { result ->
                    activities = result.documents.mapNotNull { doc ->
                        val loc = doc.getString("location")
                        val date = doc.getString("date")
                        val time = doc.getString("time")
                        if (loc != null && date != null && time != null)
                            RideActivity(loc, date, time) else null
                    }
                }
        }
    }

    Scaffold(bottomBar = { BottomNavBar(navController) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Welcome back, $username", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Image(
                        painter = painterResource(R.drawable.profile_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.Gray)
                            .clickable { navController.navigate("profile") },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.map_placeholder),
                        contentDescription = "Map",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )

                    locations.getOrNull(0)?.let { loc ->
                        MapPointer(x = 60.dp, y = 40.dp, loc) { selectedLocation = loc }
                    }

                    locations.getOrNull(1)?.let { loc ->
                        MapPointer(x = 250.dp, y = 100.dp, loc) { selectedLocation = loc }
                    }
                }
            }

            item {
                selectedLocation?.let { loc ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFB1D34B)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("From ${loc.name}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("Leaving Now ▾")
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("Number Rides Currently available: ${loc.ridesAvailable}")
                            Text("Estimated Price: 50/=")
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                                Text("Book Now", color = Color.White)
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFCDF442)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Promotions", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("You have multiple promotions available")
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { /* Handle */ },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Check Now!", color = Color.White)
                        }

                        Spacer(Modifier.height(16.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(samplePromotions) { promo -> PromotionItem(promo) }
                        }
                    }
                }
            }

            item {
                Text("Activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            if (activities.isEmpty()) {
                item {
                    Text("No recent activity.", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                items(activities) { activity ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = TealDark),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(activity.location, color = Color.White)
                                    Text("${activity.date} • ${activity.time}", color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                            Button(
                                onClick = { /* Rebook */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Re-book", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun MapPointer(x: Dp, y: Dp, location: LocationData, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(x = x, y = y)
            .clickable { onClick() }
            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(location.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.DirectionsBike, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
            Spacer(Modifier.width(4.dp))
            Text("${location.ridesAvailable} rides", color = Color.White, fontSize = 10.sp)
        }
    }
}

@Composable
fun PromotionItem(promo: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(150.dp).height(100.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(promo, color = Color.Black, fontWeight = FontWeight.SemiBold)
        }
    }
}

private val samplePromotions = listOf("25% Off", "Free Ride", "Buddy Pass", "Loyalty Bonus", "Eco Discount")
