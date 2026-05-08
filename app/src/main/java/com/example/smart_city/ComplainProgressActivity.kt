package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // CASE ID and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CASE ID: #SC-4502",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Broken Pavement",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { 0.6f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF4A67E4),
                trackColor = Color(0xFFF1F5F9),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status History Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = RoundedCornerShape(12.dp),
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

            Spacer(modifier = Modifier.height(5.dp))

            // Latest Updates Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Latest Updates",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                TextButton(
                    onClick = { },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("VIEW ALL", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A67E4))
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Admin Update Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF3F51B5), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupervisorAccount,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Admin Update",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "2h ago",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "The repair team is currently addressing the foundation issues. Road access remains partially restricted.",
                            fontSize = 14.sp,
                            color = Color(0xFF475569),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Photo Log Section
            Text(
                text = "Photo Log",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PhotoItem(
                    label = "ISSUE REPORTED",
                    imageRes = R.drawable.brokenroad,
                    modifier = Modifier.weight(1f),
                    labelBackgroundColor = Color.Black.copy(alpha = 0.5f)
                )
                PhotoItem(
                    label = "CREW AT WORK",
                    imageRes = R.drawable.crewwork,
                    modifier = Modifier.weight(1f),
                    labelBackgroundColor = Color(0xFF4A67E4).copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Add Comment Button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
            ) {
                Icon(Icons.Default.AddComment, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Add Comment", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
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
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = when {
                            isCompleted -> Color(0xFF4CAF50)
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
                    isCurrent -> Icon(Icons.Default.Engineering, null, tint = Color(0xFF3B82F6), modifier = Modifier.size(16.dp))
                    else -> Box(modifier = Modifier.size(8.dp).background(Color(0xFFCBD5E1), CircleShape))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFE2E8F0))
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(
                text = status,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = if (isCompleted || isCurrent) Color(0xFF1E293B) else Color(0xFF94A3B8)
            )
            Text(text = date, fontSize = 13.sp, color = Color(0xFF94A3B8))
            if (description != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\"$description\"",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        fontStyle = FontStyle.Italic,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoItem(label: String, imageRes: Int, modifier: Modifier = Modifier, labelBackgroundColor: Color) {
    Box(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = label,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Label overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(labelBackgroundColor)
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 10.sp,
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
                selectedIconColor = Color(0xFF1A237E),
                selectedTextColor = Color(0xFF1A237E),
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
