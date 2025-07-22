package com.example.shift_e.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.BottomNavBar
import com.example.shift_e.ui.theme.GreenDark
import com.example.shift_e.ui.theme.GreenExtraLight
import com.example.shift_e.ui.theme.GreenLight
import com.example.shift_e.ui.theme.GreenPrimary
import com.example.shift_e.ui.theme.ShadedWhite
import com.example.shift_e.ui.theme.TealDark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

data class Trip(
    val id: String,
    val location: String,
    val date: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(navController: NavController, username: String = "User") {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var trips            by remember { mutableStateOf<List<Trip>>(emptyList()) }
    var expandedTripId   by remember { mutableStateOf<String?>(null) }
    var isLoading        by remember { mutableStateOf(true) }
    val ratings          = remember { mutableStateMapOf<String, Int>() }
    var showRatingTripId by remember { mutableStateOf<String?>(null) }
    var tempRating       by remember { mutableStateOf(0) }

    // Fetch trips
    LaunchedEffect(uid) {
        if (uid != null) {
            db.collection("users")
                .document(uid)
                .collection("activities")
                .get()
                .addOnSuccessListener { result ->
                    trips = result.documents.mapNotNull { doc ->
                        val loc  = doc.getString("location")
                        val date = doc.getString("date")
                        val time = doc.getString("time")
                        if (loc != null && date != null && time != null)
                            Trip(doc.id, loc, date, time)
                        else null
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                }
        } else {
            isLoading = false
        }
    }

    // Expand first trip by default
    LaunchedEffect(trips) {
        if (trips.isNotEmpty() && expandedTripId == null) {
            expandedTripId = trips.first().id
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        0.0f to ShadedWhite,
        0.4f to GreenExtraLight,
        0.6f to GreenLight,
        1.0f to GreenDark
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Activity", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor     = ShadedWhite,
                        titleContentColor = Color.Black
                    ),
                    actions = {
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Image(
                                painter           = painterResource(R.drawable.ic_profile),
                                contentDescription = "Profile",
                                modifier          = Modifier
                                    .size(40.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .padding(4.dp)
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(navController)
            },
            containerColor = Color.Transparent
        ) { padding ->
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                if (isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TealDark)
                    }
                } else {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (trips.isEmpty()) {
                            item {
                                Text("No trips found.", color = Color.Gray)
                            }
                        } else {
                            itemsIndexed(trips) { _, trip ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            expandedTripId =
                                                if (expandedTripId == trip.id) null
                                                else trip.id
                                        },
                                    colors = CardDefaults.cardColors(containerColor = TealDark)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        if (expandedTripId == trip.id) {
                                            Image(
                                                painter           = painterResource(R.drawable.map_placeholder),
                                                contentDescription = "Trip Map",
                                                modifier          = Modifier
                                                    .fillMaxWidth()
                                                    .height(120.dp)
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale      = ContentScale.Crop
                                            )
                                            Spacer(Modifier.height(12.dp))
                                            Text(trip.location, color = Color.White, fontSize = 16.sp)
                                            Text("${trip.date} ${trip.time}", color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text("Estimated Price: LKR 50.00", color = Color.White, fontSize = 13.sp)

                                            Spacer(Modifier.height(4.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("Rating:", color = Color.White, fontSize = 13.sp)
                                                Spacer(Modifier.width(8.dp))
                                                val r = ratings[trip.id] ?: 0
                                                for (i in 1..5) {
                                                    val tint = if (i <= r) Color(0xFFFFD700) else Color.Gray
                                                    Icon(
                                                        imageVector   = Icons.Default.Star,
                                                        contentDescription = null,
                                                        tint         = tint,
                                                        modifier     = Modifier.size(20.dp)
                                                    )
                                                }
                                            }

                                            Spacer(Modifier.height(8.dp))
                                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                                Button(
                                                    onClick = {
                                                        tempRating = ratings[trip.id] ?: 0
                                                        showRatingTripId = trip.id
                                                    },
                                                    modifier = Modifier.weight(1f),
                                                    colors   = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                                ) {
                                                    Text("Rate", color = Color.White)
                                                }
                                                Button(
                                                    onClick = { navController.navigate("payment?origin=${trip.location}") },
                                                    modifier = Modifier.weight(1f),
                                                    colors   = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                                ) {
                                                    Text("Re‑book", color = Color.White)
                                                }
                                            }
                                        } else {
                                            Row(
                                                Modifier
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector   = Icons.Default.DirectionsBike,
                                                        contentDescription = null,
                                                        tint         = Color.White,
                                                        modifier     = Modifier.size(32.dp)
                                                    )
                                                    Spacer(Modifier.width(12.dp))
                                                    Column {
                                                        Text(trip.location, color = Color.White, fontSize = 14.sp)
                                                        Text("${trip.date} • ${trip.time}", color = GreenPrimary, fontSize = 12.sp)
                                                    }
                                                }
                                                Button(
                                                    onClick = { navController.navigate("payment")},
                                                    colors   = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                                    modifier = Modifier.height(35.dp)
                                                ) {
                                                    Text("Re‑book", color = Color.White, fontSize = 12.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item { Spacer(Modifier.height(100.dp)) }
                    }
                }

                if (showRatingTripId != null) {
                    AlertDialog(
                        onDismissRequest = { showRatingTripId = null },
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text      = "Enjoying Shift‑E?",
                                    style     = MaterialTheme.typography.headlineSmall,
                                    color     = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier            = Modifier.fillMaxWidth()
                            ) {
                                // <-- NEW SPACER HERE -->
                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text  = "How would you rate the ride?",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(16.dp))

                                Row {
                                    for (i in 1..5) {
                                        IconButton(onClick = { tempRating = i }) {
                                            Icon(
                                                imageVector   = Icons.Default.Star,
                                                contentDescription = "Star #$i",
                                                tint         = if (i <= tempRating) Color(0xFFFFD700) else Color.Gray,
                                                modifier     = Modifier.size(32.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                ratings[showRatingTripId!!] = tempRating
                                showRatingTripId = null
                            }) {
                                Text(text = "Submit")
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingDialog(
    initialRating: Int,
    onRatingSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var rating    by remember { mutableStateOf(initialRating) }
    var activeStar by remember { mutableStateOf<Int?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape     = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                modifier  = Modifier
                    .fillMaxWidth(0.85f)
                    .wrapContentHeight()
            ) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "How was your ride?",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        for (i in 1..5) {
                            val scale by animateFloatAsState(
                                targetValue     = if (activeStar == i) 1.4f else 1f,
                                animationSpec   = tween(300),
                                finishedListener = { activeStar = null }
                            )
                            Icon(
                                imageVector   = Icons.Default.Star,
                                contentDescription = "Star $i",
                                tint         = if (i <= rating) Color(0xFFFFD700) else Color.Gray,
                                modifier     = Modifier
                                    .size(48.dp)
                                    .scale(scale)
                                    .clickable {
                                        rating = i
                                        activeStar = i
                                    }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            onRatingSelected(rating)
                        }) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}
