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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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

class Report : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Reportbody()
        }
    }
}

@Composable
fun Reportbody(
    category: String = "Road"
) {
    var search by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ward by remember { mutableStateOf("") }
    var issueType by remember { mutableStateOf("") }
    var wardExpanded by remember { mutableStateOf(false) }
    var issueExpanded by remember { mutableStateOf(false) }

    val issueOptions = ReportData.issueOptions[category] ?: emptyList()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Color.White)
                .padding(top = 20.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(
                        text = "$category Report",
                        modifier = Modifier.align(Alignment.CenterStart),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF1A237E)
                        )
                    )
                    Icon(
                        painter = painterResource(R.drawable.baseline_notifications_24),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterEnd),
                        tint = Color(0xFF1A237E)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StepCircle("1", isSelected = true)
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE0E0E0)
                        )
                        StepCircle("2", isSelected = false)
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE0E0E0)
                        )
                        StepCircle("3", isSelected = false)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Location",
                            fontSize = 12.sp,
                            color = Color(0xFF1A237E),
                            fontWeight = FontWeight.Bold
                        )
                        Text("Details", fontSize = 12.sp, color = Color.Gray)
                        Text("Review", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "INCIDENT LOCATION",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_my_location_24),
                            contentDescription = null,
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
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // WARD DROPDOWN
            item {
                DropdownField(
                    label = "Ward",
                    selectedValue = ward,
                    options = ReportData.wards,
                    expanded = wardExpanded,
                    onExpandedChange = { wardExpanded = it },
                    onValueSelected = { ward = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // ISSUE TYPE DROPDOWN
            item {
                DropdownField(
                    label = "Issue Type",
                    selectedValue = issueType,
                    options = issueOptions,
                    expanded = issueExpanded,
                    onExpandedChange = { issueExpanded = it },
                    onValueSelected = { issueType = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Category Specific Section
            if (category == "Traffic") {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Map View for Traffic",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // AREA / LOCALITY INPUT
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    label = { Text("Area / Locality") },
                    placeholder = { Text("e.g. Baneshwor, Kalanki") },
                    shape = RoundedCornerShape(8.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Visual Evidence",
                    modifier = Modifier.padding(horizontal = 18.dp),
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Card(
                        modifier = Modifier
                            .size(110.dp)
                            .drawBehind {
                                drawRoundRect(
                                    color = Color.LightGray,
                                    style = Stroke(
                                        width = 2F,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(10f, 10f), 0f
                                        )
                                    )
                                )
                            },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painterResource(R.drawable.baseline_add_a_photo_24),
                                null,
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Add Photo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Card(
                        modifier = Modifier.size(110.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Image(
                                painterResource(R.drawable.baseline_image_24),
                                null,
                                Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(
                        "ISSUE DESCRIPTION",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("Describe the issue in detail...") },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                ElevatedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xFF0D2A77),
                        contentColor = Color.White
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Submit Report",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(painterResource(R.drawable.baseline_arrow_forward_24), null)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun StepCircle(number: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = if (isSelected) Color(0xFF1A237E) else Color(0xFFF5F5F5),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReportPreview() {
    Reportbody()
}
