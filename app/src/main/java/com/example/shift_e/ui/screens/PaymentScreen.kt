package com.example.shift_e.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.BottomNavBar
import com.example.shift_e.ui.components.BoxContainer
import com.example.shift_e.ui.components.CardInfo
import com.example.shift_e.ui.components.PaymentCardManager
import com.example.shift_e.ui.components.PaymentMethodPopup
import com.example.shift_e.ui.enums.PaymentOption
import com.example.shift_e.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val location1 = "nsbm"
    val location2 = "school_junction"
    var origin by remember { mutableStateOf(location1) }
    var destination by remember { mutableStateOf(location2) }

    var ridesAvailable by remember { mutableStateOf(0) }
    var charging by remember { mutableStateOf(0) }
    var onTheWay by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var showCardManager by remember { mutableStateOf(false) }
    val cards = remember { mutableStateListOf<CardInfo>() }
    var selectedCardId by remember { mutableStateOf<String?>(null) }

    // Fetch availability data when origin changes
    LaunchedEffect(origin) {
        loading = true
        db.collection("locations").document(origin).get()
            .addOnSuccessListener { doc ->
                ridesAvailable = doc.getLong("ridesAvailable")?.toInt() ?: 0
                charging = doc.getLong("charging")?.toInt() ?: 0
                onTheWay = doc.getLong("ontheway")?.toInt() ?: 0
                loading = false
            }
            .addOnFailureListener {
                loading = false
            }
    }

    var selectedOption by remember { mutableStateOf(PaymentOption.CARD) }
    var showDialog by remember { mutableStateOf(false) }

    val backgroundBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to ShadedWhite,
            0.4f to GreenExtraLight,
            0.6f to GreenLight,
            1.0f to GreenDark
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(brush = backgroundBrush)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0),
            topBar = {
                TopAppBar(
                    title = { Text("Trip Confirmation", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ShadedWhite,
                        titleContentColor = Color.Black
                    ),
                    actions = {
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Image(
                                painter = painterResource(R.drawable.ic_profile),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .padding(4.dp),
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(navController)
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Location box
                BoxContainer(bgColor = GrayLight, height = 150.dp) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .width(16.dp)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(modifier = Modifier.size(16.dp).background(TealDark, shape = MaterialTheme.shapes.small))
                            Spacer(modifier = Modifier.height(4.dp))
                            Canvas(modifier = Modifier.width(2.dp).height(60.dp)) {
                                drawLine(
                                    color = Color.Gray,
                                    start = Offset(x = size.width / 2, y = 0f),
                                    end = Offset(x = size.width / 2, y = size.height),
                                    strokeWidth = 2f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.size(16.dp).background(GreenDark, shape = RectangleShape))
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            TextBox(origin.replace('_', ' ').uppercase())
                            Spacer(Modifier.height(8.dp))
                            Text("To", fontSize = 15.sp, color = Color.Black)
                            Spacer(Modifier.height(8.dp))
                            TextBox(destination.replace('_', ' ').uppercase())
                        }

                        IconButton(
                            onClick = {
                                val tmp = origin
                                origin = destination
                                destination = tmp
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SwapVert,
                                contentDescription = "Swap Locations",
                                tint = Color.Gray,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // QR Scan Section
                BoxContainer(bgColor = ForestGreen, height = 170.dp) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text("Scan Bike QR", fontSize = 16.sp, color = Color.White)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.width(10.dp))
                            Image(
                                painter = painterResource(R.drawable.ic_qr),
                                contentDescription = "Bike QR",
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(Modifier.width(25.dp))
                            Text("Located at the Stem of the bike handle", fontSize = 15.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                            Button(onClick = { navController.navigate("qrscanner") }, colors = ButtonDefaults.buttonColors(containerColor = TealDark)) {
                                Text("Scan")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Payment method section
                BoxContainer(bgColor = TealDark, height = 190.dp) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Payment Method", fontSize = 16.sp, color = Color.White)
                            Divider(color = Color.White.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.width(140.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Selected Option", fontSize = 15.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(Modifier.height(4.dp))
                            Text(
                                when (selectedOption) {
                                    PaymentOption.CARD   -> "Credit / Debit Card"
                                    PaymentOption.WALLET  -> "Wallet"
                                    PaymentOption.PAYPAL -> "PayPal"
                                },
                                fontSize = 16.sp, color = Color.White
                            )
                            Spacer(Modifier.height(20.dp))
                            Button(onClick = { showDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = GreenDark)) {

                                Text("Choose", color = Color.White)
                                Spacer(Modifier.width(4.dp))
                                Icon(Icons.Default.ChevronRight, contentDescription = "Choose", tint = Color.White)
                            }
                        }

                        val iconRes = when (selectedOption) {
                            PaymentOption.CARD   -> R.drawable.ic_credit_card
                            PaymentOption.WALLET   -> R.drawable.ic_wallet
                            PaymentOption.PAYPAL -> R.drawable.ic_paypal
                        }

                        Image(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(width = 160.dp, height = 120.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Current availability section
                BoxContainer(bgColor = TealDark, height = 160.dp) {
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Current Availability", fontSize = 16.sp, color = Color.White)
                            Divider(color = Color.White.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.width(150.dp).padding(vertical = 4.dp))
                            Spacer(Modifier.height(4.dp))

                            if (loading) {
                                Text("Loading...", color = Color.LightGray)
                            } else {
                                Text("Ready to go: $ridesAvailable", fontSize = 15.sp, color = Color.Green)
                                Text("Charging: $charging", fontSize = 15.sp, color = Color.Red)
                                Text("On the way: $onTheWay", fontSize = 15.sp, color = Color.White)
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_bike),
                                contentDescription = "Bike",
                                modifier = Modifier.size(240.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    if (showDialog) {
        PaymentMethodPopup(
            selectedOption   = selectedOption,
            onOptionSelected = { selectedOption = it },
            onCardDetails    = {
                showDialog = false
                showCardManager = true
            },
            onDismiss        = { showDialog = false },
            modifier         = Modifier
                .fillMaxSize()
                .zIndex(1f)
        )
    }
    if (showCardManager) {
        PaymentCardManager(
            cards           = cards,
            selectedCardId  = selectedCardId,
            onCardSelected  = { selectedCardId = it },
            onAddCard       = { cards.add(it); selectedCardId = it.id },
            onDismiss       = { showCardManager = false },
            modifier        = Modifier.fillMaxSize().zIndex(2f)
        )
    }
}

@Composable
private fun TextBox(text: String) {
    Box(
        modifier = Modifier
            .border(BorderStroke(1.dp, TealDark), shape = MaterialTheme.shapes.large)
            .background(TealDark.copy(alpha = 0.1f), shape = MaterialTheme.shapes.large)
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Text(text = text, fontSize = 14.sp, color = TealDark)
    }
}
