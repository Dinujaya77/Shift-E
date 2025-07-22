package com.example.shift_e.ui.screens

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.shift_e.ui.theme.Charcoal
import com.example.shift_e.ui.theme.ShadowBlack
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.json.JSONObject
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrScannerScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var isScanning by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    when (cameraPermissionState.status) {
        is PermissionStatus.Granted -> {
            if (isScanning) {
                val previewView = remember { PreviewView(context) }
                val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

                DisposableEffect(previewView) {
                    val executor = Executors.newSingleThreadExecutor()
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = androidx.camera.core.Preview.Builder()
                            .build()
                            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                        val scanner = BarcodeScanning.getClient()
                        val analysisUseCase = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { analysis ->
                                analysis.setAnalyzer(executor) { imageProxy ->
                                    processImageProxy(scanner, imageProxy) { qr ->
                                        try {
                                            val json = JSONObject(qr)
                                            val key = json.optString("Key")
                                            val bikeId = json.optInt("BikeID", -1)

                                            if (key == "qgwjerty1234" && bikeId != -1) {
                                                Log.d("QrScanner", "Valid QR with BikeID: $bikeId")
                                                isScanning = false
                                                navController.navigate("driverscreen?bikeId=$bikeId")
                                            } else {
                                                Log.w("QrScanner", "Invalid QR Code content.")
                                            }
                                        } catch (e: Exception) {
                                            Log.e("QrScanner", "Invalid JSON format", e)
                                        }
                                    }
                                }
                            }

                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            analysisUseCase
                        )
                    }, ContextCompat.getMainExecutor(context))

                    onDispose { executor.shutdown() }
                }

                val config = LocalConfiguration.current
                val screenWidth = config.screenWidthDp.dp
                val screenHeight = config.screenHeightDp.dp
                val targetSize = (screenWidth * 0.6f).coerceAtMost(300.dp)
                val verticalPadding = (screenHeight - targetSize) / 2
                val horizontalPadding = (screenWidth - targetSize) / 2

                Box(Modifier.fillMaxSize()) {
                    AndroidView({ previewView }, Modifier.fillMaxSize())

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(verticalPadding)
                            .background(ShadowBlack)
                            .align(Alignment.TopCenter)
                    )

                    Row(
                        Modifier.align(Alignment.Center)
                    ) {
                        Box(
                            Modifier
                                .width(horizontalPadding)
                                .height(targetSize)
                                .background(ShadowBlack)
                        )
                        Box(
                            Modifier
                                .size(targetSize)
                                .border(2.dp, Color.White)
                        )
                        Box(
                            Modifier
                                .width(horizontalPadding)
                                .height(targetSize)
                                .background(ShadowBlack)
                        )
                    }

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(verticalPadding)
                            .background(ShadowBlack)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }

        is PermissionStatus.Denied -> {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Camera permission is required to scan QR codes")
                Spacer(Modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant permission")
                }
            }
        }
    }
}

private fun processImageProxy(
    scanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    imageProxy: ImageProxy,
    onQrFound: (String) -> Unit
) {
    @androidx.camera.core.ExperimentalGetImage
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes
                    .mapNotNull(Barcode::getRawValue)
                    .firstOrNull()
                    ?.let(onQrFound)
            }
            .addOnCompleteListener { imageProxy.close() }
    } else {
        imageProxy.close()
    }
}
