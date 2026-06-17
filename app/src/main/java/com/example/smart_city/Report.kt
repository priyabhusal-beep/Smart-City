package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.smart_city.viewmodel.ReportViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.smart_city.util.LocationHelper
import com.example.smart_city.util.GeoCoderHelper

class Report : ComponentActivity() {
    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineLocation =
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false

            val coarseLocation =
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

//            if (fineLocation || coarseLocation) {
//                // Permission granted

            android.util.Log.d(
                "PERMISSION_DEBUG",
                "Fine=$fineLocation, Coarse=$coarseLocation"
            )

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Reported(navController = navController)
        }
    }
}

@Composable
fun Reported(
    navController: NavHostController,
    category: String = "Road",
    viewModel: ReportViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val issueOptions = ReportData.issueOptions[category] ?: emptyList()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = backgroundColor)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(
                        text = "$category Report",
                        modifier = Modifier.align(Alignment.CenterStart),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1A237E))
                    )
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
                                android.util.Log.d("GPS_TEST", "GOT LAT=$lat LNG=$lng")


                                viewModel.latitude = lat
                                viewModel.longitude = lng

                                android.util.Log.d(
                                    "VM_TEST",
                                    "VM LAT=${viewModel.latitude} LNG=${viewModel.longitude}"
                                )

                                viewModel.searchArea =
                                    GeoCoderHelper.getAddress(
                                        context,
                                        lat,
                                        lng
                                    )

                                Toast.makeText(
                                    context,
                                    "Location Detected",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },

                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "Failed to get location",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(
                            R.drawable.baseline_my_location_24
                        ),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "Detect My Location",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = Color(0xFF1E88E5),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text("INCIDENT LOCATION", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp, color = secondaryTextColor))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(painter = painterResource(R.drawable.baseline_my_location_24), contentDescription = null)
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text("Detect My Location", style = TextStyle(fontSize = 13.sp, color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold))
//                    }
//                }
            }

            item {
                DropdownField(
                    label = "Ward",
                    selectedValue = viewModel.ward,
                    options = ReportData.wards,
                    expanded = wardExpanded,
                    onExpandedChange = { wardExpanded = it },
                    onValueSelected = { viewModel.ward = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            item {
                DropdownField(
                    label = "Issue Type",
                    selectedValue = viewModel.issueType,
                    options = issueOptions,
                    expanded = issueExpanded,
                    onExpandedChange = { issueExpanded = it },
                    onValueSelected = { viewModel.issueType = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    OutlinedTextField(
                        value = viewModel.searchArea,
                        onValueChange = { viewModel.searchArea = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Area / Locality", color = textColor) },
                        placeholder = { Text("Search Area...", color = secondaryTextColor) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = cardBackgroundColor,
                            unfocusedContainerColor = cardBackgroundColor
                        )
                    )

                    val suggestions = viewModel.getFilteredAreas()
                    if (suggestions.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            suggestions.forEach { area ->
                                TextButton(
                                    onClick = {
                                        viewModel.searchArea = area
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(area, color = Color.Black, modifier = Modifier.align(Alignment.CenterVertically))
                                }
                            }
                        }
                    }
                }
            }

            if (category == "Traffic") {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            FullMapscreen(
                                complaints = emptyList()
                            )
                            Button(
                                onClick = { },
                                modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                                shape = RoundedCornerShape(50)
                            ) { Text("View Full Map") }
                        }
                    }
                }
            }

            item {
                Column {
                    Text("Visual Evidence", modifier = Modifier.padding(horizontal = 18.dp), style = TextStyle(fontSize = 13.sp, color = secondaryTextColor, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Card(
                            modifier = Modifier.size(110.dp).drawBehind {
                                drawRoundRect(color = Color.LightGray, style = Stroke(width = 2F, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)))
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(painterResource(R.drawable.baseline_add_a_photo_24), null, tint = secondaryTextColor)
                                Text("Add Photo", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = secondaryTextColor)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Card(modifier = Modifier.size(110.dp), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFE0E0E0))) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Image(painterResource(R.drawable.baseline_image_24), null, Modifier.size(40.dp))
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text("ISSUE DESCRIPTION", style = TextStyle(fontSize = 12.sp, color = secondaryTextColor, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.description,
                        onValueChange = { viewModel.description = it },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        placeholder = { Text("Describe the issue in detail...") },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = cardBackgroundColor,
                            focusedContainerColor = cardBackgroundColor
                        )
                    )
                }
            }

            item {
                ElevatedButton(
                    onClick = {
                        viewModel.submit(category) { resultMessage ->
                            successMessage = resultMessage
                            showSuccessPopup = true

                            // Auto-hide popup after 3 seconds
                            scope.launch {
                                delay(3000)
                                showSuccessPopup = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF0D2A77), contentColor = Color.White)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Submit Report", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(painterResource(R.drawable.baseline_arrow_forward_24), null)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        // Success Popup (appears on same screen)
        if (showSuccessPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (successMessage.contains("Successfully")) "✅" else "❌",
                            fontSize = 64.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = successMessage,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { showSuccessPopup = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D2A77))
                        ) {
                            Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepCircle(number: String, isSelected: Boolean, isDarkMode: Boolean = false) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = if (isSelected) Color(0xFF1A237E) else if (isDarkMode) Color(0xFF333333) else Color(0xFFF5F5F5),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = if (isSelected) Color.White else if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReportPreview() {
    val navController = rememberNavController()
    Reported(
        navController = navController,
        category = "Road"
    )
}