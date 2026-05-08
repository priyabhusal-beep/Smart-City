package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme

class ComplainProgressActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                ComplainProgressScreen()
            }
        }
    }
}

@Composable
fun ComplainProgressScreen() {
    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFBFBFE))
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Case ID and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CASE ID: #SC-4502",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFEBF2FF),
                    modifier = Modifier.height(26.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF3B82F6), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "In Progress",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3B82F6)
                        )
                    }
                }
            }

            Text(
                text = "Broken Pavement",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            // Progress Bar
            LinearProgressIndicator(
                progress = { 0.6f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF4A67E4),
                trackColor = Color(0xFFE5E7EB),
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Status History Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFF3F4F6))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Status History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    StatusTimelineItem(
                        status = "Reported",
                        date = "Oct 10, 2023 • 09:15 AM",
                        isCompleted = true
                    )
                    StatusTimelineItem(
                        status = "In Progress",
                        date = "Oct 12, 2023 • 02:45 PM",
                        description = "Crew dispatched to location. Work started on main structural repair.",
                        isCurrent = true
                    )
                    StatusTimelineItem(
                        status = "Resolved",
                        date = "Estimated Oct 15, 2023",
                        isLast = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Latest Updates Header

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StatusTimelineItem(
    status: String,
    date: String,
    description: String? = null,
    isCompleted: Boolean = false,
    isCurrent: Boolean = false,
    isLast: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = when {
                            isCompleted -> Color(0xFF22C55E)
                            isCurrent -> Color(0xFFDBEAFE)
                            else -> Color(0xFFF1F5F9)
                        },
                        shape = CircleShape
                    )
                    .border(
                        width = if (isCurrent) 2.dp else 0.dp,
                        color = if (isCurrent) Color(0xFF3B82F6) else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCompleted -> Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    isCurrent -> Icon(Icons.Default.Build, null, tint = Color(0xFF3B82F6), modifier = Modifier.size(16.dp))
                    else -> Box(modifier = Modifier.size(8.dp).background(Color(0xFFCBD5E1), CircleShape))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(if (description != null) 100.dp else 40.dp)
                        .background(Color(0xFFF1F5F9))
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = status,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = if (isCompleted || isCurrent) Color(0xFF1E293B) else Color(0xFF94A3B8)
            )
            Text(text = date, fontSize = 13.sp, color = Color.Gray)
            if (description != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\"$description\"",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp,
                        color = Color(0xFF475569),
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PhotoItem(label: String, modifier: Modifier = Modifier, color: Color) {
    Box(
        modifier = modifier
            .height(140.dp)
            .background(color, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Label at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 12.sp) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color(0xFF94A3B8),
                unselectedTextColor = Color(0xFF94A3B8)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = "Reports") },
            label = { Text("Reports", fontSize = 12.sp) },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E3A8A),
                selectedTextColor = Color(0xFF1E3A8A),
                indicatorColor = Color(0xFFEBF2FF)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 12.sp) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color(0xFF94A3B8),
                unselectedTextColor = Color(0xFF94A3B8)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComplainProgressPreview() {
    SmartCityTheme {
        ComplainProgressScreen()
    }
}
