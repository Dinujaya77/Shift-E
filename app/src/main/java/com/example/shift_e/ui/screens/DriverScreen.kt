package com.example.shift_e.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import android.location.Location
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.BottomNavBar
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverScreen(navController: NavController, username: String = "user") {
    val context = LocalContext.current

    var speedKmph by remember { mutableStateOf(0.0) }
    var distanceTraveled by remember { mutableStateOf(0.0) }
    var tripStartTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var durationMinutes by remember { mutableStateOf(0L) }
    var cost by remember { mutableStateOf(50) }
    var lastLocation by remember { mutableStateOf<Location?>(null) }

    var azimuth by remember { mutableStateOf(0f) }
    val rotationDegrees by animateFloatAsState(targetValue = -azimuth, label = "arrow_rotation")

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val locationRequest = remember {
        LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
    }

    DisposableEffect(Unit) {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                speedKmph = location.speed * 3.6
                lastLocation?.let {
                    distanceTraveled += it.distanceTo(location)
                }
                lastLocation = location

                val now = System.currentTimeMillis()
                durationMinutes = TimeUnit.MILLISECONDS.toMinutes(now - tripStartTime)
                cost = 50 + durationMinutes.toInt()
            }
        }

        startLocationUpdates(context, fusedLocationClient, locationRequest, callback)
        onDispose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        System.arraycopy(event.values, 0, gravity, 0, 3)
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                    }
                }

                if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accel, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, magnet, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF3C5E45), Color(0xFF6D9F7B), Color(0xFF1B1B1B))
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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Trip Information",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("\uD83C\uDFC1 1.8 km", color = Color.White, fontSize = 18.sp)
                        Text("Duration: $durationMinutes mins", color = Color.White, fontSize = 14.sp)
                        Text("Distance: ${"%.2f".format(distanceTraveled / 1000)} km", color = Color.White, fontSize = 14.sp)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${speedKmph.toInt()}",
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
                            modifier = Modifier.size(48.dp).rotate(rotationDegrees)
                        )
                        Text("${"%.2f".format(distanceTraveled / 1000)} km", color = Color.White, fontSize = 14.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1B3C1A), RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cost: LKR $cost",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

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
                            text = "After exceeding the limit of the school junction drop off point cost will be added with respect to Distance and Time.",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D4D3A)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Selected Payment Method", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.card_2),
                            contentDescription = "Visa Card",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("VISA •••• 1234", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                }

                OutlinedButton(
                    onClick = {
                        navController.popBackStack()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF520000))
                ) {
                    Text("End Trip", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun startLocationUpdates(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    request: LocationRequest,
    callback: LocationCallback
) {
    fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
}
