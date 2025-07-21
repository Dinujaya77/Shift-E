package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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

data class Trip(
    val id: String,
    val location: String,
    val date: String,
    val time: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(navController: NavController, username: String = "User") {
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var trips by remember { mutableStateOf<List<Trip>>(emptyList()) }
    var expandedTripId by remember { mutableStateOf<String?>(null) }

    // Fetch trips from Firestore
    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("users").document(uid).collection("activities").get()
                .addOnSuccessListener { result ->
                    trips = result.documents.mapNotNull { doc ->
                        val loc = doc.getString("location")
                        val date = doc.getString("date")
                        val time = doc.getString("time")
                        if (loc != null && date != null && time != null)
                            Trip(doc.id, loc, date, time)
                        else null
                    }
                }
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
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
                    Text(
                        "Past Trips",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Image(
                        painter = painterResource(R.drawable.profile_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { navController.navigate("profile") }
                    )
                }
            }

            if (trips.isEmpty()) {
                item {
                    Text("No trips found.", color = Color.Gray)
                }
            } else {
                itemsIndexed(trips) { index, trip ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedTripId = if (expandedTripId == trip.id) null else trip.id
                            },
                        colors = CardDefaults.cardColors(containerColor = TealDark)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (expandedTripId == trip.id) {
                                // Expanded card
                                Image(
                                    painter = painterResource(R.drawable.map_placeholder),
                                    contentDescription = "Trip Map",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(trip.location, color = Color.White, fontSize = 16.sp)
                                Text("${trip.date} ${trip.time}", color = Color.Gray, fontSize = 13.sp)
                                Text("Estimated Price: LKR 50.00", color = Color.White, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Button(
                                        onClick = { /* Rate */ },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                    ) {
                                        Text("Rate", color = Color.White)
                                    }
                                    Button(
                                        onClick = { /* Rebook */ },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                    ) {
                                        Text("Re-book", color = Color.White)
                                    }
                                }
                            } else {
                                // Collapsed card
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.DirectionsBike,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(trip.location, color = Color.White, fontSize = 14.sp)
                                            Text("${trip.date} • ${trip.time}", color = Color.Gray, fontSize = 12.sp)
                                        }
                                    }
                                    Button(
                                        onClick = { /* Rebook */ },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text("Re-book", color = Color.White, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}
