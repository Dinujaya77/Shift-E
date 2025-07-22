package com.example.shift_e.ui.screens.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.model.getProfileImageRes
import com.example.shift_e.ui.components.BottomNavBar
import com.example.shift_e.ui.theme.*
import com.example.shift_e.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val user by userViewModel.userData.collectAsState()
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var locations by remember { mutableStateOf<List<LocationData>>(emptyList()) }
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }
    var activities by remember { mutableStateOf<List<RideActivity>>(emptyList()) }

    LaunchedEffect(Unit) {
        userViewModel.loadUserData()
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

        uid?.let {
            db.collection("users").document(it).collection("activities").get()
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

    val backgroundBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to GreenDark,
            0.6f to GreenLight,
            1.0f to GreenExtraLight
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(brush = backgroundBrush)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.welcome_back, user.firstName),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.Black
                    ),
                    actions = {
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Image(
                                painter = painterResource(user.getProfileImageRes()),
                                contentDescription = stringResource(R.string.profile_desc),
                                modifier = Modifier.size(40.dp).clip(CircleShape).border(1.dp, Color.Gray, CircleShape).padding(2.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                )
            },
            bottomBar = { BottomNavBar(navController) }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(1.dp)) }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.map_placeholder),
                            contentDescription = stringResource(R.string.map_desc),
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop,
                            alpha = 0.95f
                        )
                        locations.getOrNull(1)?.let {
                            MapPointer(x = 60.dp, y = 40.dp, location = it) { selectedLocation = it }
                        }
                        locations.getOrNull(0)?.let {
                            MapPointer(x = 250.dp, y = 100.dp, location = it) { selectedLocation = it }
                        }
                    }
                }

                item {
                    selectedLocation?.let { loc ->
                        MinimalCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(stringResource(R.string.rides_from, loc.name), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(stringResource(R.string.rides_available, loc.ridesAvailable), fontSize = 14.sp, color = Color.White)
                                Text(stringResource(R.string.estimated_price), fontSize = 14.sp, color = Color.White)
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(onClick = { navController.navigate("payment?origin=${loc.id}") }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black), modifier = Modifier.fillMaxWidth()) {
                                    Text(stringResource(R.string.book_now), color = Color.White)
                                }
                            }
                        }
                    }
                }

                item {
                    Text(stringResource(R.string.promotions), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }
                item { PromotionCard() }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color(0xFF10382A), shape = RoundedCornerShape(24.dp)).padding(20.dp)
                    ) {
                        Text(text = stringResource(R.string.activity), fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))

                        if (activities.isEmpty()) {
                            Text(stringResource(R.string.no_recent_rides), color = Color.Black, fontSize = 16.sp)
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                activities.forEach { activity ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(18.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(activity.location, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                                                Text("${activity.date} • ${activity.time}", fontSize = 12.sp, color = Color.Gray)
                                            }
                                            Button(onClick = { navController.navigate("payment?origin=${activity.location}") }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black), shape = RoundedCornerShape(50)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(stringResource(R.string.rebook), color = Color.White, fontSize = 12.sp)
                                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MinimalCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF10382A))
    ) {
        Column(content = content, modifier = Modifier.background(Color(0xFF10382A)))
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
            Icon(
                Icons.Default.DirectionsBike,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.White
            )
            Spacer(Modifier.width(4.dp))
            Text("${location.ridesAvailable} rides", color = Color.White, fontSize = 10.sp)
        }
    }
}

@Composable
fun PromotionCard() {
    val promotions = listOf(
        "25% Off",
        "Free Ride",
        "Buddy Pass",
        "Loyalty Bonus",
        "Eco Discount"
    )

    val listState = rememberLazyListState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - 85.dp
    val density = LocalDensity.current
    val centerItemIndex = remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    val selectedPromo = promotions[centerItemIndex.value]


    // Initial scroll to center
    LaunchedEffect(Unit) {
        listState.scrollToItem(centerItemIndex.value)
    }

    // Center item snapping logic
    LaunchedEffect(
        listState.firstVisibleItemIndex,
        listState.firstVisibleItemScrollOffset
    ) {
        val layoutInfo = listState.layoutInfo
        val center = layoutInfo.viewportEndOffset / 2

        val closest = layoutInfo.visibleItemsInfo.minByOrNull { item ->
            kotlin.math.abs((item.offset + item.size / 2) - center)
        }

        closest?.let {
            if (centerItemIndex.value != it.index) {
                centerItemIndex.value = it.index

                val screenWidthPx = with(density) { screenWidth.toPx() }
                val itemOffsetPx = (screenWidthPx / 2 - it.size / 2).toInt()

                listState.animateScrollToItem(index = it.index, scrollOffset = -itemOffsetPx)
            }
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(promotions.size) { index ->
            val isSelected = index == centerItemIndex.value
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.1f else 0.95f,
                label = "CardScale"
            )

            val backgroundColor = if (isSelected) GreenDark else GreenPrimary

            Card(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(24.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
            ) {
                Box(
                    modifier = Modifier
                        .width(screenWidth * 0.85f)
                        .height(120.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = "Promotion Icon",
                                tint = if (isSelected) Color.Black else Color.DarkGray,
                                modifier = Modifier.size(if (isSelected) 30.dp else 24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = promotions[index],
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                fontSize = if (isSelected) 20.sp else 16.sp,
                                color = Color.Black
                            )
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { showDialog = true },
                                shape = RoundedCornerShape(40),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier
                                    .height(32.dp)
                                    .defaultMinSize(minWidth = 90.dp)
                            ) {
                                Text(
                                    "Check Now!",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                    }
                }
            }
        }
    }
    if (showDialog) {
        PromotionDetailDialog(promotionTitle = selectedPromo) {
            showDialog = false
        }
    }

}

@Composable
fun PromotionDetailDialog(
    promotionTitle: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = Color(0xFF008F4C),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = promotionTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Enjoy exclusive rewards with our $promotionTitle offer. Apply at checkout to receive your benefits!",
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "This promotion is coming soon!",
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Got it!", color = Color.White)
                }
            }
        }
    }
}


private val samplePromotions =
    listOf("25% Off", "Free Ride", "Buddy Pass", "Loyalty Bonus", "Eco Discount")

data class LocationData(val id: String, val name: String, val ridesAvailable: Int)
data class RideActivity(val location: String, val date: String, val time: String)
