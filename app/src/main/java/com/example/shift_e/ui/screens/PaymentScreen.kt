package com.example.shift_e.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.BottomNavBar
import com.example.shift_e.ui.components.BoxContainer
import com.example.shift_e.ui.components.PaymentMethodDialog
import com.example.shift_e.ui.enums.PaymentOption
import com.example.shift_e.ui.theme.ForestGreen
import com.example.shift_e.ui.theme.GreenDark
import com.example.shift_e.ui.theme.GreenLight
import com.example.shift_e.ui.theme.GrayLight
import com.example.shift_e.ui.theme.GreenExtraLight
import com.example.shift_e.ui.theme.ShadedWhite
import com.example.shift_e.ui.theme.TealDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController) {
    val location1 = "NSBM Green University"
    val location2 = "School Junction"
    var origin by remember { mutableStateOf(location1) }
    var destination by remember { mutableStateOf(location2) }
    val readyCount = 12
    val chargingCount = 3
    val onTheWayCount = 5
    var selectedOption by remember { mutableStateOf(PaymentOption.CARD) }
    var showDialog by remember { mutableStateOf(false) }
    val backgroundBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f  to ShadedWhite,
            0.4f  to GreenExtraLight,
            0.6f  to GreenLight,
            1.0f  to GreenDark
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
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
                BoxContainer(
                    bgColor = GrayLight,
                    height = 150.dp
                ) {
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
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(TealDark, shape = MaterialTheme.shapes.small)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Canvas(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(60.dp)
                            ) {
                                drawLine(
                                    color = Color.Gray,
                                    start = Offset(x = size.width / 2, y = 0f),
                                    end = Offset(x = size.width / 2, y = size.height),
                                    strokeWidth = 2f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(GreenDark, shape = RectangleShape)
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .border(BorderStroke(1.dp, TealDark), shape = MaterialTheme.shapes.large)
                                    .background(TealDark.copy(alpha = 0.1f), shape = MaterialTheme.shapes.large)
                                    .padding(horizontal = 20.dp, vertical = 6.dp)
                            ) {
                                Text(text = origin, fontSize = 14.sp, color = TealDark)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(text = "To", fontSize = 15.sp, color = Color.Black)
                            Spacer(Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .border(BorderStroke(1.dp, TealDark), shape = MaterialTheme.shapes.large)
                                    .background(TealDark.copy(alpha = 0.1f), shape = MaterialTheme.shapes.large)
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(text = destination, fontSize = 14.sp, color = TealDark)
                            }
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
                                tint = Color.Gray, modifier = Modifier.size(size = 50.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                BoxContainer(
                    bgColor = ForestGreen,
                    height = 170.dp
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Scan Bike QR",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(Modifier.width(10.dp))
                            Image(
                                painter = painterResource(R.drawable.ic_qr),
                                contentDescription = "Bike QR",
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(Modifier.width(25.dp))
                            Text(
                                text = "Located at the Stem of the bike handle",
                                fontSize = 15.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Button(
                                onClick = {  navController.navigate("qrscanner")},
                                colors = ButtonDefaults.buttonColors(containerColor = TealDark)
                            ) {
                                Text(text = "Scan")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

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
                                    PaymentOption.CASH   -> "Cash"
                                    PaymentOption.PAYPAL -> "PayPal"
                                },
                                fontSize = 16.sp, color = Color.White
                            )
                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = { showDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenDark)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                                Spacer(Modifier.width(4.dp))
                                Text("Edit", color = Color.White)
                            }
                        }
                        val iconRes = when (selectedOption) {
                            PaymentOption.CARD   -> R.drawable.ic_credit_card
                            PaymentOption.CASH   -> R.drawable.ic_cash
                            PaymentOption.PAYPAL -> R.drawable.ic_paypal
                        }
                        Image(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(width = 160.dp, height = 100.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))

                BoxContainer(
                    bgColor = TealDark,
                    height = 160.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Current Availability", fontSize = 16.sp, color = Color.White)
                            Divider(
                                color = Color.White.copy(alpha = 0.5f),
                                thickness = 1.dp,
                                modifier = Modifier
                                    .width(150.dp)
                                    .padding(vertical = 4.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Ready to go: $readyCount",
                                fontSize = 15.sp,
                                color = Color.Green
                            )
                            Text(
                                text = "Charging: $chargingCount",
                                fontSize = 15.sp,
                                color = Color.Red
                            )
                            Text(
                                text = "On the way: $onTheWayCount",
                                fontSize = 15.sp,
                                color = Color.White
                            )
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

            if (showDialog) {
                PaymentMethodDialog(
                    selectedOption = selectedOption,
                    onOptionSelected = {
                        selectedOption = it
                    },
                    onDismiss = {
                        showDialog = false
                    }
                )
            }
        }
    }
}
