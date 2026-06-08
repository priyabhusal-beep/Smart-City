package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


val PrimaryBlue = Color(0xFF0046B1)
val AccentTeal = Color(0xFF00A389)
val LightBlueBg = Color(0xFFF0F5FF)
val BackgroundGray = Color(0xFFF8F9FA)

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize MapLibre before setContent

        
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                var isDarkMode by remember { mutableStateOf(false) }
                val mainNavController = rememberNavController()
                HomeActivity(
                    navController = mainNavController,
                    isDarkMode = isDarkMode,
                    onDarkModeToggle = { updatedValue -> isDarkMode = updatedValue })
            }
        }
    }
}

data class RecentReportData(
    val image: Int,
    val title: String,
    val location: String,
    val time: String,
    val status: String,
    val color: Color
)

@Composable
fun HomeActivity(
    navController: NavController,
    isDarkMode: Boolean,              // 👈 Add this line
    onDarkModeToggle: (Boolean) -> Unit
) {
    // State to track which screen tab is currently active
    var selectedIndex by remember { mutableStateOf(0) }
    val innerNavController = rememberNavController()
    // ✅ ADD THIS: Define colors based on dark mode
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color.White
    val cardBackgroundColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    NavHost(
        navController = innerNavController,
        startDestination = "home"
    ) {
        composable("home") {

            Scaffold(
                bottomBar = {
                    Surface(
                        shadowElevation = 8.dp,
                        color = backgroundColor
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // TAB 0: HOME
                            TextButton(onClick = { selectedIndex = 0 }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_home_24),
                                        contentDescription = "Home",
                                        tint = if (selectedIndex == 0) PrimaryBlue else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Home",
                                        fontSize = 10.sp,
                                        color = if (selectedIndex == 0) PrimaryBlue else Color.Gray,
                                        fontWeight = if (selectedIndex == 0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }

                            // TAB 1: REPORTS
                            TextButton(onClick = { selectedIndex = 1 }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_report_24),
                                        contentDescription = "Reports",
                                        tint = if (selectedIndex == 1) PrimaryBlue else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Reports",
                                        fontSize = 10.sp,
                                        color = if (selectedIndex == 1) PrimaryBlue else Color.Gray,
                                        fontWeight = if (selectedIndex == 1) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                            // TAB 1: COMPLAINTS

                            TextButton(onClick = { selectedIndex = 2 }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(R.drawable.complaint),
                                        contentDescription = "Complaints",
                                        tint = if (selectedIndex == 2) PrimaryBlue else Color.Gray,  // ← Remove tint override so original colors show
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Complaints",
                                        fontSize = 10.sp,
                                        color = if (selectedIndex == 2) PrimaryBlue else Color.Gray,
                                        fontWeight = if (selectedIndex == 2) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }

                            // TAB 2: PROFILE
                            TextButton(onClick = { selectedIndex = 3 }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_person_outline_24),
                                        contentDescription = "Profile",
                                        tint = if (selectedIndex == 3) PrimaryBlue else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Profile",
                                        fontSize = 10.sp,
                                        color = if (selectedIndex == 3) PrimaryBlue else Color.Gray,
                                        fontWeight = if (selectedIndex == 3) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(backgroundColor)
                ) {
                    // Switches between the views dynamically based on tab clicks
                    when (selectedIndex) {
                        0 -> DashboardContents(innerNavController, isDarkMode, backgroundColor, cardBackgroundColor, textColor, secondaryTextColor)
                        1 -> Reportbody("Traffic", isDarkMode, backgroundColor, cardBackgroundColor, textColor, secondaryTextColor)
                        2 -> ComplainActivity(isDarkMode, backgroundColor, cardBackgroundColor, textColor, secondaryTextColor)
                        3 -> UserprofileBody(navController = navController, isDarkMode = isDarkMode, onDarkModeToggle = onDarkModeToggle)
                    }
                }
            }
        }
        composable("report/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Traffic"
            Reportbody(category)
        }
        composable("FullMap") {
            FullMapscreen()
        }
    }
}

        // Your main layout logic has been moved cleanly into this component
        @Composable
        fun DashboardContents(
            navController: androidx.navigation.NavController,
            isDarkMode: Boolean = false,
            backgroundColor: Color = Color.White,
            cardBackgroundColor: Color = Color(0xFFF5F5F5),
            textColor: Color = Color.Black,
            secondaryTextColor: Color = Color.Gray
        ) {
            var search by remember { mutableStateOf("") }

            val recentReport = listOf(
                RecentReportData(
                    image = R.drawable.road,
                    title = "Broken Pavement",
                    location = "12th Ave, North Block",
                    time = "2h ago",
                    status = "In Progress",
                    color = PrimaryBlue
                ),
                RecentReportData(
                    image = R.drawable.garbage,
                    title = "Overflowing Bin",
                    location = "Central Park East",
                    time = "3h ago",
                    status = "Pending",
                    color = PrimaryBlue
                )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(horizontal = 20.dp)
            ) {
                // Header
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.lana),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(50.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Good Morning", color = secondaryTextColor)
                            Text(
                                text = "Lana Del",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }
                        Icon(
                            painter = painterResource(R.drawable.baseline_notifications_24),
                            contentDescription = null,
                            tint = PrimaryBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search complaints...") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_search_24),
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Quick Actions
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ReportCard(
                            modifier = Modifier.weight(1f),
                            image = R.drawable.others,
                            label = "Report"
                        )
                        ReportCard(
                            modifier = Modifier.weight(1f),
                            image = R.drawable.track,
                            label = "Track"
                        )
                        ReportCard(
                            modifier = Modifier.weight(1f),
                            image = R.drawable.map,
                            label = "Nearby"
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Categories
                item {
                    Text(
                        text = "Categories",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ReportCard(Modifier.weight(1f),
                            R.drawable.road,
                            "Road",
                            onClick = {navController.navigate("report/Road")})
                        ReportCard(Modifier.weight(1f),
                            R.drawable.garbage,
                            "Garbage",
                            onClick = {navController.navigate("report/Garbage")})
                        ReportCard(Modifier.weight(1f),
                            R.drawable.traffic,
                            "Traffic",
                            onClick = {navController.navigate("report/Traffic")})

                        ReportCard(Modifier.weight(1f),
                            R.drawable.map,
                            "Others",
                            onClick = {navController.navigate("report/Others")})
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Map Visual Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Box (
                            modifier = Modifier.fillMaxSize()
                        ){
                         MapScreen()


                            Button(
                                onClick = {
                                    navController.navigate("FullMap")

                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),

                            shape= RoundedCornerShape(50)
                            ){
                                Text("View Full Map")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Recent Report Section Title
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Recent Reports",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Text(text = "View All", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Dynamic feed lists
                items(recentReport) { report ->
                    RecentReportCard(report)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        @Composable
        fun ReportCard(modifier: Modifier, image: Int, label: String,
                       backgroundColor: Color = Color.White,  // ✅ ADD
                       textColor: Color = Color.Black,
                       onClick:() ->Unit ={}) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.size(50.dp),
                    onClick= onClick,
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(image),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = label, fontSize = 13.sp, color = PrimaryBlue)
            }
        }

        @Composable
        fun RecentReportCard(report: RecentReportData,
                             cardBackgroundColor: Color = Color.White,  // ✅ ADD
                             textColor: Color = Color.Black ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(report.image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = report.title, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        Text(text = report.location, color = Color.Gray, fontSize = 13.sp)
                        Text(text = report.time, color = Color.Gray, fontSize = 12.sp)
                    }
                    Text(
                        text = report.status,
                        color = report.color,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }



// Placeholders - Rename these to match your actual file functions if they are different
@Composable fun ReportScreen() { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Report Screen View") } }
@Composable fun UserprofileScreen() { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Profile Screen View") } }

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    SmartCityTheme {
        HomeActivity(navController = rememberNavController(),
            isDarkMode = false,
            onDarkModeToggle = {})
    }
}
