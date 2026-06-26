package com.example.smart_city

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smart_city.util.GeoCoderHelper
import com.example.smart_city.util.LocationHelper
import com.example.smart_city.viewmodel.ImageViewModel
import com.example.smart_city.viewmodel.ReportViewModel
import com.example.smart_city.viewmodel.TrafficViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class Report : ComponentActivity() {
    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val camera = permissions[android.Manifest.permission.CAMERA] ?: false
            val fineLocation = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocation = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            android.util.Log.d(
                "PERMISSION_DEBUG",
                "Fine=$fineLocation, Coarse=$coarseLocation, Camera=$camera"
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA
            )
        )

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Reported(navController = navController)
        }
    }
}

// ✅ Safer Image Uri Helper for Reports
fun getReportImageUri(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val folder = File(context.cacheDir, "report_images")
        if (!folder.exists()) folder.mkdirs()
        val file = File(folder, "report_${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
        Uri.fromFile(file)
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Reported(
    navController: NavHostController,
    category: String = "Road",
    viewModel: ReportViewModel = viewModel(),
    imageViewModel: ImageViewModel = viewModel(),
    isDarkMode: Boolean = false, 
    backgroundColor: Color = Color.White,
    cardBackgroundColor: Color = Color(0xFFF5F5F5),
    textColor: Color = Color.Black,
    secondaryTextColor: Color = Color.Gray
) {
    var wardExpanded by remember { mutableStateOf(false) }
    var issueExpanded by remember { mutableStateOf(false) }
    var showSuccessPopup by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val trafficViewModel: TrafficViewModel = viewModel()

    // Launchers
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            viewModel.capturedImage = bitmap
            getReportImageUri(context, bitmap)?.let { uri ->
                imageViewModel.uploadImage(context, uri) { url -> if (url != null) viewModel.imageUrl = url }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
            viewModel.capturedImage = bitmap
            imageViewModel.uploadImage(context, it) { url -> if (url != null) viewModel.imageUrl = url }
        }
    }

    LaunchedEffect(Unit){
        trafficViewModel.fetchTraffic()
    }
    val issueOptions = ReportData.issueOptions[category] ?: emptyList()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(backgroundColor).padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text("$category Report", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1A237E)))
                    Icon(
                        painter = painterResource(R.drawable.baseline_notifications_24),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterEnd),
                        tint = Color(0xFF1A237E)
                    )
                }
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        StepCircle("1", isSelected = true, isDarkMode = isDarkMode)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = if (isDarkMode) Color(0xFF444444) else Color(0xFFE0E0E0))
                        StepCircle("2", isSelected = false, isDarkMode = isDarkMode)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = if (isDarkMode) Color(0xFF444444) else Color(0xFFE0E0E0))
                        StepCircle("3", isSelected = false, isDarkMode = isDarkMode)
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Location", fontSize = 12.sp, color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
                        Text("Details", fontSize = 12.sp, color = secondaryTextColor)
                        Text("Review", fontSize = 12.sp, color = secondaryTextColor)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.clickable {
                        LocationHelper.getLocation(
                            context = context,
                            callback = { lat, lng ->
                                viewModel.latitude = lat
                                viewModel.longitude = lng
                                viewModel.searchArea = GeoCoderHelper.getAddress(context, lat, lng)
                            },
                            onFailure = {
                                Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(R.drawable.baseline_my_location_24), null, tint = Color(0xFF1E88E5))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Detect My Location", color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold)
                }
            }

            item {
                DropdownField("Ward", viewModel.ward, ReportData.wards, wardExpanded, { wardExpanded = it }, { viewModel.ward = it }, Modifier.fillMaxWidth().padding(horizontal = 16.dp))
            }

            item {
                val options = ReportData.issueOptions[category] ?: emptyList()
                DropdownField("Issue Type", viewModel.issueType, options, issueExpanded, { issueExpanded = it }, { viewModel.issueType = it }, Modifier.fillMaxWidth().padding(horizontal = 16.dp))
            }

            item {
                OutlinedTextField(viewModel.searchArea, { viewModel.searchArea = it }, Modifier.fillMaxWidth().padding(horizontal = 16.dp), label = { Text("Area / Locality") }, shape = RoundedCornerShape(8.dp))
            }

            if (category == "Traffic") {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            TrafficMapScreen(trafficList = trafficViewModel.trafficList)
                            Button(
                                onClick = { context.startActivity(Intent(context, TrafficMapActivity::class.java)) },
                                modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                                shape = RoundedCornerShape(50)
                            ) { Text("View Full Map") }
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Text("Visual Evidence", fontWeight = FontWeight.Bold, color = secondaryTextColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Card(
                            Modifier.size(110.dp).clickable { showImageSourceDialog = true }.drawBehind {
                                drawRoundRect(Color.LightGray, style = Stroke(2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)))
                            },
                            shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(Color.Transparent)
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(painterResource(R.drawable.baseline_add_a_photo_24), null, tint = secondaryTextColor)
                                Text("Add Photo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = secondaryTextColor)
                            }
                        }
                        Card(Modifier.size(110.dp), shape = RoundedCornerShape(8.dp)) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                if (viewModel.capturedImage != null) Image(bitmap = viewModel.capturedImage!!.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                else Image(painterResource(R.drawable.baseline_image_24), null, Modifier.size(40.dp))
                            }
                        }
                    }
                }
            }

            item {
                OutlinedTextField(viewModel.description, { viewModel.description = it }, Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp), placeholder = { Text("Description...") }, shape = RoundedCornerShape(8.dp))
            }

            item {
                Button(
                    onClick = { viewModel.submit(category) { msg -> successMessage = msg; showSuccessPopup = true; scope.launch { delay(3000); showSuccessPopup = false } } },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0D2A77)), shape = RoundedCornerShape(12.dp)
                ) { Text("Submit Report", fontWeight = FontWeight.Bold) }
            }
        }

        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                title = { Text("Choose Source") },
                text = {
                    Column {
                        ListItem(headlineContent = { Text("Camera") }, leadingContent = { Icon(Icons.Default.PhotoCamera, null) }, modifier = Modifier.clickable { 
                            showImageSourceDialog = false
                            cameraLauncher.launch()
                        })
                        ListItem(headlineContent = { Text("Gallery") }, leadingContent = { Icon(Icons.Default.PhotoLibrary, null) }, modifier = Modifier.clickable { 
                            showImageSourceDialog = false
                            galleryLauncher.launch("image/*") 
                        })
                    }
                },
                confirmButton = { TextButton(onClick = { showImageSourceDialog = false }) { Text("Cancel") } }
            )
        }

        if (showSuccessPopup) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(0.5f)), contentAlignment = Alignment.Center) {
                Card(Modifier.fillMaxWidth(0.8f).padding(16.dp), RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = if (successMessage.contains("Successfully")) "✅" else "❌", fontSize = 48.sp)
                        Text(successMessage, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showSuccessPopup = false }, colors = ButtonDefaults.buttonColors(Color(0xFF0D2A77))) { Text("OK", color = Color.White) }
                    }
                }
            }
        }
    }
}

@Composable
fun StepCircle(number: String, isSelected: Boolean, isDarkMode: Boolean = false) {
    Box(
        modifier = Modifier.size(32.dp).background(color = if (isSelected) Color(0xFF1A237E) else if (isDarkMode) Color(0xFF333333) else Color(0xFFF5F5F5), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = number, color = if (isSelected) Color.White else if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
