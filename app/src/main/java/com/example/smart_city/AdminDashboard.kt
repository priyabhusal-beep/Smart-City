package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.smart_city.ui.theme.SmartCityTheme

class AdminDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

        setContent {
            SmartCityTheme {
                AdminDashboardScreen()
            }
        }
    }
}

@Composable
fun AdminDashboardScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA),
        bottomBar = { CustomBottomNavigation() }
    ) { innerPadding ->
        DashboardContent(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 24.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp
        )
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Text("Admin Dashboard", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D236D))
                Text("Real-time infrastructure oversight", color = Color.Gray, fontSize = 14.sp)
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Total Complaints", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("124", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.width(12.dp))
                        Text("+12% vs LW", color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatBox("45", "Pending", Icons.Default.Info, Modifier.weight(1f), Color(0xFFFF8A65))
                StatBox("32", "In Progress", Icons.Default.Build, Modifier.weight(1f), Color(0xFF64B5F6))
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Resolved", color = Color.Gray, fontSize = 12.sp)
                        Text("47", color = Color(0xFF2E7D32), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                    Surface(shape = CircleShape, color = Color(0xFFE8F5E9), modifier = Modifier.size(44.dp)) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.padding(10.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Complaints Trend", fontWeight = FontWeight.Bold, color = Color(0xFF0D236D), fontSize = 16.sp)
                    Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                        Text("Chart Placeholder", color = Color.LightGray, fontSize = 14.sp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Column {
                Row(
                    Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Urgent Complaints", fontWeight = FontWeight.Bold, color = Color(0xFF0D236D), fontSize = 18.sp)
                    Text("View All", color = Color(0xFF1E3A8A), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(modifier = Modifier.padding(top = 6.dp).size(8.dp).background(Color.Red, CircleShape))
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("Water Leakage - Ward 4", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
                            Text("2h ago", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(value: String, label: String, icon: ImageVector, modifier: Modifier, iconColor: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = iconColor)
            Spacer(Modifier.height(12.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun CustomBottomNavigation() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = Color.White,
        shadowElevation = 24.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            NavigationBarItem(
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Default.Home, null, modifier = Modifier.size(28.dp)) },
                label = { Text("Admin Home", fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0D236D),
                    selectedTextColor = Color(0xFF0D236D),
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = { Icon(Icons.Default.Person, null, modifier = Modifier.size(24.dp)) },
                label = { Text("Manage") },
                colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.LightGray)
            )
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = { Icon(Icons.Default.List, null, modifier = Modifier.size(24.dp)) },
                label = { Text("Analytics") },
                colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.LightGray)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun FullPreview() {
    SmartCityTheme {
        AdminDashboardScreen()
    }
}