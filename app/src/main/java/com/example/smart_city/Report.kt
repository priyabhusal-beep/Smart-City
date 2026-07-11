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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smart_city.ui.theme.SmartCityTheme
import com.example.smart_city.util.GeoCoderHelper
import com.example.smart_city.util.LocationHelper
import com.example.smart_city.utils.ThemePreference
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

        val isDarkMode = ThemePreference.getDarkMode(this)

        setContent {
            SmartCityTheme {
                val navController = rememberNavController()

                Reported(
                    navController = navController,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

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
    isDarkMode: Boolean = false
) {
    var wardExpanded by remember { mutableStateOf(false) }
    var issueExpanded by remember { mutableStateOf(false) }
    var showSuccessPopup by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val trafficViewModel: TrafficViewModel = viewModel()

    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color.White
    val cardBackgroundColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
    val textColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val borderColor = if (isDarkMode) Color(0xFF444444) else Color.LightGray

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = cardBackgroundColor,
        unfocusedContainerColor = cardBackgroundColor,
        focusedBorderColor = PrimaryBlue,
        unfocusedBorderColor = borderColor,
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedLabelColor = PrimaryBlue,
        unfocusedLabelColor = secondaryTextColor,
        focusedPlaceholderColor = secondaryTextColor,
        unfocusedPlaceholderColor = secondaryTextColor,
        cursorColor = PrimaryBlue
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                viewModel.capturedImage = bitmap
                viewModel.imageUrl = ""

                getReportImageUri(context, bitmap)?.let { uri ->
                    imageViewModel.uploadImage(context, uri) { url ->
                        if (url != null) {
                            viewModel.imageUrl = url
                        } else {
                            Toast.makeText(
                                context,
                                "Image upload failed, please retry",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }

                viewModel.capturedImage = bitmap
                viewModel.imageUrl = ""

                imageViewModel.uploadImage(context, it) { url ->
                    if (url != null) {
                        viewModel.imageUrl = url
                    } else {
                        Toast.makeText(
                            context,
                            "Image upload failed, please retry",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    LaunchedEffect(Unit) {
        trafficViewModel.fetchTraffic()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundColor
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "$category Report",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = if (isDarkMode) Color.White else PrimaryBlue
                        )
                    )

                    Icon(
                        painter = painterResource(R.drawable.baseline_notifications_24),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterEnd),
                        tint = if (isDarkMode) Color.White else PrimaryBlue
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StepCircle("1", isSelected = true, isDarkMode = isDarkMode)

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = if (isDarkMode) Color(0xFF444444) else Color(0xFFE0E0E0)
                        )

                        StepCircle("2", isSelected = false, isDarkMode = isDarkMode)

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = if (isDarkMode) Color(0xFF444444) else Color(0xFFE0E0E0)
                        )

                        StepCircle("3", isSelected = false, isDarkMode = isDarkMode)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Location",
                            fontSize = 12.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Details", fontSize = 12.sp, color = secondaryTextColor)
                        Text("Review", fontSize = 12.sp, color = secondaryTextColor)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .clickable {
                            LocationHelper.getLocation(
                                context = context,
                                callback = { lat, lng ->
                                    viewModel.latitude = lat
                                    viewModel.longitude = lng
                                    viewModel.searchArea =
                                        GeoCoderHelper.getAddress(context, lat, lng)
                                },
                                onFailure = {
                                    Toast.makeText(
                                        context,
                                        "Failed to get location",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_my_location_24),
                        contentDescription = null,
                        tint = Color(0xFF1E88E5)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "Detect My Location",
                        color = Color(0xFF1E88E5),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                ReportDropdownField(
                    label = "Ward",
                    selectedValue = viewModel.ward,
                    options = ReportData.wards,
                    expanded = wardExpanded,
                    onExpandedChange = { wardExpanded = it },
                    onValueSelected = { viewModel.ward = it },
                    isDarkMode = isDarkMode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            item {
                val options = ReportData.issueOptions[category] ?: emptyList()

                ReportDropdownField(
                    label = "Issue Type",
                    selectedValue = viewModel.issueType,
                    options = options,
                    expanded = issueExpanded,
                    onExpandedChange = { issueExpanded = it },
                    onValueSelected = { viewModel.issueType = it },
                    isDarkMode = isDarkMode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = viewModel.searchArea,
                    onValueChange = { viewModel.searchArea = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    label = { Text("Area / Locality") },
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
            }

            if (category == "Traffic") {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkMode) Color(0xFF242424) else Color(0xFFE0E0E0)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TrafficMapScreen(trafficList = trafficViewModel.trafficList)

                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent(context, TrafficMapActivity::class.java)
                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryBlue
                                ),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("View Full Map")
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Visual Evidence",
                        fontWeight = FontWeight.Bold,
                        color = secondaryTextColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Card(
                            modifier = Modifier
                                .size(110.dp)
                                .clickable { showImageSourceDialog = true }
                                .drawBehind {
                                    drawRoundRect(
                                        color = if (isDarkMode) Color(0xFF666666) else Color.LightGray,
                                        style = Stroke(
                                            width = 2f,
                                            pathEffect = PathEffect.dashPathEffect(
                                                floatArrayOf(10f, 10f),
                                                0f
                                            )
                                        )
                                    )
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painterResource(R.drawable.baseline_add_a_photo_24),
                                    contentDescription = null,
                                    tint = secondaryTextColor
                                )

                                Text(
                                    "Add Photo",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = secondaryTextColor
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.size(110.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = cardBackgroundColor
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (viewModel.capturedImage != null) {
                                    Image(
                                        bitmap = viewModel.capturedImage!!.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    if (imageViewModel.isUploading) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.4f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                color = Color.White,
                                                strokeWidth = 2.dp,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_image_24),
                                        contentDescription = null,
                                        tint = secondaryTextColor,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (imageViewModel.isUploading) {
                        Text(
                            "Uploading image, please wait...",
                            fontSize = 11.sp,
                            color = Color(0xFF1E88E5),
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = viewModel.description,
                    onValueChange = { viewModel.description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(horizontal = 16.dp),
                    placeholder = { Text("Description...") },
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
            }

            item {
                Button(
                    onClick = {
                        if (viewModel.capturedImage != null && viewModel.imageUrl.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please wait for the image to finish uploading before submitting",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        viewModel.submit(category) { msg ->
                            successMessage = msg
                            showSuccessPopup = true

                            scope.launch {
                                delay(3000)
                                showSuccessPopup = false
                            }
                        }
                    },
                    enabled = !imageViewModel.isUploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (imageViewModel.isUploading) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Uploading image...", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Text("Submit Report", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                containerColor = cardBackgroundColor,
                titleContentColor = textColor,
                textContentColor = textColor,
                title = { Text("Choose Source") },
                text = {
                    Column {
                        ListItem(
                            headlineContent = { Text("Camera") },
                            leadingContent = {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = cardBackgroundColor,
                                headlineColor = textColor,
                                leadingIconColor = textColor
                            ),
                            modifier = Modifier.clickable {
                                showImageSourceDialog = false
                                cameraLauncher.launch()
                            }
                        )

                        ListItem(
                            headlineContent = { Text("Gallery") },
                            leadingContent = {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = cardBackgroundColor,
                                headlineColor = textColor,
                                leadingIconColor = textColor
                            ),
                            modifier = Modifier.clickable {
                                showImageSourceDialog = false
                                galleryLauncher.launch("image/*")
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showImageSourceDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showSuccessPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardBackgroundColor
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (successMessage.contains("Successfully")) "✅" else "❌",
                            fontSize = 48.sp
                        )

                        Text(
                            text = successMessage,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showSuccessPopup = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBlue
                            )
                        ) {
                            Text("OK", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepCircle(
    number: String,
    isSelected: Boolean,
    isDarkMode: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = if (isSelected) {
                    PrimaryBlue
                } else if (isDarkMode) {
                    Color(0xFF333333)
                } else {
                    Color(0xFFF5F5F5)
                },
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = if (isSelected) {
                Color.White
            } else if (isDarkMode) {
                Color(0xFFB0B0B0)
            } else {
                Color.Gray
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onValueSelected: (String) -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val borderColor = if (isDarkMode) Color(0xFF444444) else Color.LightGray

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(it) },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cardColor,
                unfocusedContainerColor = cardColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedLabelColor = PrimaryBlue,
                unfocusedLabelColor = secondaryTextColor,
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = borderColor,
                cursorColor = PrimaryBlue
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            containerColor = cardColor
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = textColor
                        )
                    },
                    onClick = {
                        onValueSelected(option)
                        onExpandedChange(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}