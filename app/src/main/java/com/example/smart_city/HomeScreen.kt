package com.example.smart_city

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smart_city.repo.AuthRepository
import com.example.smart_city.viewmodel.AuthViewModel
import com.example.smart_city.viewmodel.AuthViewModelFactory
import com.example.smart_city.model.ReportModel
import com.example.smart_city.viewmodel.ComplaintsViewModel
import java.text.SimpleDateFormat
import java.util.Date


val PrimaryBlue = Color(0xFF0046B1)
val AccentTeal = Color(0xFF00A389)
val LightBlueBg = Color(0xFFF0F5FF)
val BackgroundGray = Color(0xFFF8F9FA)


class HomeScreen : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                var isDarkMode by remember { mutableStateOf(false) }
                val mainNavController = rememberNavController()
                HomeActivity(
                    navController = mainNavController,
                    isDarkMode = isDarkMode,
                    onDarkModeToggle = { newValue -> isDarkMode = newValue },
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun LoadCurrentUserEffect(authViewModel: AuthViewModel?) {
    LaunchedEffect(Unit) {
        try {
            val authRepository = AuthRepository()
            val currentUser = authRepository.getCurrentUser()
            authViewModel?.setCurrentUser(currentUser)
            Log.d("HomeScreen", "User loaded: ${currentUser.name}")
        } catch (e: Exception) {
            Log.d("HomeScreen", "No user logged in - using guest mode")
        }
    }
}

@Composable
fun HomeActivity(
    navController: NavController,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    authViewModel: AuthViewModel? = null
) {
    LoadCurrentUserEffect(authViewModel)
    var selectedIndex by remember { mutableStateOf(0) }
    val innerNavController = rememberNavController()
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color.White
    val cardBackgroundColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    NavHost(navController = innerNavController, startDestination = "home") {
        composable("home") {
            Scaffold(
                bottomBar = {
                    Surface(shadowElevation = 8.dp, color = backgroundColor) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { selectedIndex = 0 }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(painter = painterResource(R.drawable.baseline_home_24), contentDescription = "Home", tint = if (selectedIndex == 0) PrimaryBlue else Color.Gray, modifier = Modifier.size(24.dp))
                                    Text("Home", fontSize = 10.sp, color = if (selectedIndex == 0) PrimaryBlue else Color.Gray, fontWeight = if (selectedIndex == 0) FontWeight.Bold else FontWeight.Normal)
                                }
                            }

                            TextButton(onClick = { selectedIndex = 1 }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(painter = painterResource(R.drawable.baseline_report_24), contentDescription = "Reports", tint = if (selectedIndex == 1) PrimaryBlue else Color.Gray, modifier = Modifier.size(24.dp))
                                    Text("Reports", fontSize = 10.sp, color = if (selectedIndex == 1) PrimaryBlue else Color.Gray, fontWeight = if (selectedIndex == 1) FontWeight.Bold else FontWeight.Normal)
                                }
                            }

                            TextButton(onClick = { selectedIndex = 2 }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(painter = painterResource(R.drawable.complaint), contentDescription = "Complaints", tint = if (selectedIndex == 2) PrimaryBlue else Color.Gray, modifier = Modifier.size(24.dp))
                                    Text("Complaints", fontSize = 10.sp, color = if (selectedIndex == 2) PrimaryBlue else Color.Gray, fontWeight = if (selectedIndex == 2) FontWeight.Bold else FontWeight.Normal)
                                }
                            }

                            TextButton(onClick = { selectedIndex = 3 }) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(painter = painterResource(R.drawable.baseline_person_outline_24), contentDescription = "Profile", tint = if (selectedIndex == 3) PrimaryBlue else Color.Gray, modifier = Modifier.size(24.dp))

                                    Text("Profile", fontSize = 10.sp, color = if (selectedIndex == 3) PrimaryBlue else Color.Gray, fontWeight = if (selectedIndex == 3) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding).background(backgroundColor)) {
                    when (selectedIndex) {
                        0 -> DashboardContents(innerNavController, isDarkMode, backgroundColor, cardBackgroundColor, textColor, secondaryTextColor, authViewModel)
                        1 -> Reported(navController = innerNavController, category = "Traffic", isDarkMode = isDarkMode, backgroundColor = backgroundColor, cardBackgroundColor = cardBackgroundColor, textColor = textColor, secondaryTextColor = secondaryTextColor)
                        2 -> ComplaintsListScreen(innerNavController, isDarkMode, backgroundColor, cardBackgroundColor, textColor, secondaryTextColor)
                        3 -> UserprofileBody(isDarkMode = isDarkMode, onDarkModeToggle = onDarkModeToggle, authViewModel = authViewModel)
                    }
                }
            }
        }

        composable("all_complaints") {
            AllComplaintsScreen(innerNavController, isDarkMode, backgroundColor, cardBackgroundColor, textColor, secondaryTextColor)
        }

        composable("report/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Traffic"
            Reported(navController = innerNavController, category = category)
        }

        composable("FullMap") {
            FullMapscreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllComplaintsScreen(
    navController: NavController,
    isDarkMode: Boolean,
    backgroundColor: Color,
    cardBackgroundColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    viewModel: ComplaintsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val complaints = viewModel.complaints
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.fetchAllComplaints()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Complaints", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(backgroundColor).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    }
                }
            } else if (complaints.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("No complaints yet", color = secondaryTextColor, fontSize = 16.sp)
                    }
                }
            } else {
                items(complaints) { complaint ->
                    ComplaintItemCard(complaint, cardBackgroundColor, textColor, secondaryTextColor)
                }
            }
        }
    }
}

@Composable
fun ComplaintsListScreen(
    navController: NavController,
    isDarkMode: Boolean,
    backgroundColor: Color,
    cardBackgroundColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    viewModel: ComplaintsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val complaints = viewModel.complaints
    val isLoading = viewModel.isLoading

    LaunchedEffect(Unit) {
        viewModel.fetchAllComplaints()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(backgroundColor).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            }
        } else if (complaints.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No complaints yet", color = secondaryTextColor, fontSize = 16.sp)
                }
            }
        } else {
            items(complaints) { complaint ->
                ComplaintItemCard(complaint, cardBackgroundColor, textColor, secondaryTextColor)
            }
        }
    }
}

@Composable
fun ComplaintItemCard(
    complaint: ReportModel,
    cardBackgroundColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = complaint.category, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    Text(text = "Issue: ${complaint.issueType}", fontSize = 13.sp, color = secondaryTextColor)
                }
                AssistChip(onClick = {}, label = { Text(complaint.category, fontSize = 11.sp) })
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Location: ${complaint.area}, ${complaint.ward}", fontSize = 12.sp, color = secondaryTextColor)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Description: ${complaint.description}", fontSize = 12.sp, color = textColor, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(complaint.timestamp)), fontSize = 11.sp, color = secondaryTextColor)
        }
    }
}

@Composable
fun DashboardContents(
    navController: NavController,
    isDarkMode: Boolean = false,
    backgroundColor: Color = Color.White,
    cardBackgroundColor: Color = Color(0xFFF5F5F5),
    textColor: Color = Color.Black,
    secondaryTextColor: Color = Color.Gray,
    authViewModel: AuthViewModel? = null,
    viewModel: ComplaintsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var search by remember { mutableStateOf("") }
    val currentUser by authViewModel?.currentUser?.collectAsState() ?: remember { mutableStateOf(null) }
    val complaints = viewModel.complaints
    val isLoading = viewModel.isLoading

    val expandedCategories = remember { mutableStateOf(setOf<String>()) }

    val complaintsByCategory = remember(complaints) {
        complaints.groupBy { it.category }.toSortedMap()
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllComplaints()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(backgroundColor).padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.lana), contentDescription = null, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(50.dp)))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Hello", color = secondaryTextColor)
                    Text(text = currentUser?.name ?: "Guest", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                }
                Icon(painter = painterResource(R.drawable.baseline_notifications_24), contentDescription = null, tint = PrimaryBlue)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search complaints...") },
                leadingIcon = { Icon(painter = painterResource(R.drawable.baseline_search_24), contentDescription = null) },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color.LightGray)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ReportCard(modifier = Modifier.weight(1f), image = R.drawable.others, label = "Report Issue", onClick = { navController.navigate("report/Others") })
                ReportCard(modifier = Modifier.weight(1f), image = R.drawable.track, label = "Track Issue", onClick = { navController.navigate("all_complaints") })
                ReportCard(modifier = Modifier.weight(1f), image = R.drawable.map, label = "Nearby Issues", onClick = { navController.navigate("FullMap") })
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(text = "Categories", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ReportCard(Modifier.weight(1f), R.drawable.road, "Road", onClick = { navController.navigate("report/Road") })
                ReportCard(Modifier.weight(1f), R.drawable.garbage, "Garbage", onClick = { navController.navigate("report/Garbage") })
                ReportCard(Modifier.weight(1f), R.drawable.traffic, "Traffic", onClick = { navController.navigate("report/Traffic") })
                ReportCard(Modifier.weight(1f), R.drawable.map, "Others", onClick = { navController.navigate("report/Others") })
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Card(modifier = Modifier.fillMaxWidth().height(190.dp), shape = RoundedCornerShape(18.dp)) {
                Box(modifier = Modifier.fillMaxSize()) {
                    FullMapscreen()
                    Button(onClick = { navController.navigate("FullMap") }, modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue), shape = RoundedCornerShape(50)) {
                        Text("View Full Map")
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Recent Reports", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                TextButton(onClick = { navController.navigate("all_complaints") }) {
                    Text(text = "View All", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            }
        } else if (complaintsByCategory.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No reports yet", color = secondaryTextColor)
                }
            }
        } else {
            complaintsByCategory.forEach { (category, categoryComplaints) ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val current = expandedCategories.value
                                expandedCategories.value = if (current.contains(category)) {
                                    current - category
                                } else {
                                    current + category
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$category (${categoryComplaints.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }
                }

                if (expandedCategories.value.contains(category)) {
                    items(categoryComplaints) { complaint ->
                        RecentReportCardClickable(complaint, cardBackgroundColor, textColor, secondaryTextColor) { navController.navigate("all_complaints") }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun ReportCard(modifier: Modifier, image: Int, label: String, backgroundColor: Color = Color.White, textColor: Color = Color.Black, onClick:() ->Unit ={}) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Card(modifier = Modifier.size(50.dp), onClick= onClick, shape = RoundedCornerShape(18.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(image), contentDescription = null, modifier = Modifier.size(60.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, color = PrimaryBlue, textAlign = TextAlign.Center)
    }
}

@Composable
fun RecentReportCardClickable(
    complaint: ReportModel,
    cardBackgroundColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            val iconRes = when (complaint.category.lowercase()) {
                "road" -> R.drawable.road
                "garbage" -> R.drawable.garbage
                "traffic" -> R.drawable.traffic
                else -> R.drawable.map
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(iconRes), contentDescription = null, modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = complaint.issueType, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryBlue)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Area: ", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = textColor)
                Text(text = complaint.area, fontSize = 12.sp, color = secondaryTextColor)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Ward: ", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = textColor)
                Text(text = complaint.ward, fontSize = 12.sp, color = secondaryTextColor)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Status: ", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = textColor)
                AssistChip(
                    onClick = {},
                    label = { Text(complaint.status, fontSize = 11.sp) },
                    modifier = Modifier.height(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Description: ", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = textColor)
            }
            Text(text = complaint.description, fontSize = 12.sp, color = secondaryTextColor, maxLines = 3)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(complaint.timestamp)),
                fontSize = 11.sp,
                color = secondaryTextColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    SmartCityTheme {
        HomeActivity(navController = rememberNavController(), isDarkMode = false, onDarkModeToggle = {})
    }
}