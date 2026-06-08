package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class Userprofile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityApp()
        }
    }
}

// ============ MAIN APP WITH DARK MODE SUPPORT ============
@Composable
fun SmartCityApp() {
    // Dark mode state managed at app level
    var isDarkMode by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    // Define colors for light and dark mode
    val lightBackground = Color(0xFFF8F9FA)
    val darkBackground = Color(0xFF121212)
    val lightCardBackground = Color.White
    val darkCardBackground = Color(0xFF1E1E1E)
    val lightText = Color.Black
    val darkText = Color(0xFFE0E0E0)

    // Create color scheme
    val colors = if (isDarkMode) {
        darkColorScheme(
            primary = Color(0xFF1A237E),
            surface = darkBackground,
            onSurface = darkText
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF1A237E),
            surface = lightBackground,
            onSurface = lightText
        )
    }

    MaterialTheme(colorScheme = colors) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (isDarkMode) darkBackground else lightBackground
        ) {
            NavHost(navController, startDestination = "profile_screen") {
                composable("profile_screen") {
                    UserprofileBody(
                        navController,
                        isDarkMode,
                        onDarkModeToggle = { isDarkMode = it }
                    )
                }
                composable("complaints_screen") {
                    ComplaintsScreen(navController, isDarkMode)
                }
                composable("settings_screen") {
                    SettingsPrivacyScreen(navController, isDarkMode)
                }
                composable("help_screen") {
                    HelpSupportScreen(navController, isDarkMode)
                }
            }
        }
    }
}

// ============ MAIN PROFILE SCREEN ============
@Composable
fun UserprofileBody(
    navController: NavController,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = backgroundColor)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SmartCity",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xFF1A237E),
                        fontWeight = FontWeight.Bold
                    )
                )

                Icon(
                    painter = painterResource(R.drawable.baseline_notifications_24),
                    contentDescription = null,
                    tint = Color(0xFF1A237E)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {

                    Box(
                        modifier = Modifier
                            .size(115.dp)
                            .border(width = 3.dp, color = Color(0xFF00B8D4), shape = CircleShape)
                            .padding(6.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.user),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color(0xFF4CAF50), CircleShape)
                            .border(width = 3.dp, color = Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Alex Johnson",
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF2C3E50) else Color(0xFFE8EAF6)
                    )
                ) {
                    Text(
                        text = "Top Contributor",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color(0xFF3F51B5),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Statistics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem("12", "REPORTS", Color.Blue)
                StatItem("45", "UPVOTES", Color.Blue)
                StatItem("8", "RESOLVED", Color(0xFF4CAF50))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Menu Items Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    // My Complaints Button
                    MenuRow(
                        icon = painterResource(R.drawable.outline_assignment_24),
                        title = "My Complaints",
                        iconContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        iconColor = secondaryTextColor,
                        textColor = textColor,
                        onClick = { navController.navigate("complaints_screen") }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
                    )

                    // Settings & Privacy Button
                    MenuRow(
                        icon = Icons.Default.Settings,
                        title = "Settings & Privacy",
                        iconContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        iconColor = secondaryTextColor,
                        textColor = textColor,
                        onClick = { navController.navigate("settings_screen") }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
                    )

                    // Dark Mode Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_dark_mode_24),
                                contentDescription = null,
                                tint = secondaryTextColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Dark Mode",
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { onDarkModeToggle(it) }
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
                    )

                    // Help & Support Button
                    MenuRow(
                        icon = Icons.Default.Info,
                        title = "Help & Support",
                        iconContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        iconColor = secondaryTextColor,
                        textColor = textColor,
                        onClick = { navController.navigate("help_screen") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Logout Button
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ============ MY COMPLAINTS SCREEN ============
@Composable
fun ComplaintsScreen(navController: NavController, isDarkMode: Boolean) {
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    // Sample complaints data
    val complaints = listOf(
        Complaint("Road pothole near market", "Open", "2024-01-15"),
        Complaint("Street light broken", "In Progress", "2024-01-20"),
        Complaint("Water supply issue", "Resolved", "2024-01-25"),
        Complaint("Garbage not collected", "Open", "2024-02-01")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(28.dp),
                tint = Color(0xFF1A237E)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "My Complaints",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        // Complaints List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(complaints.size) { index ->
                ComplaintCard(
                    complaint = complaints[index],
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
            }
        }
    }
}

@Composable
fun ComplaintCard(
    complaint: Complaint,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = complaint.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${complaint.status}",
                    fontSize = 13.sp,
                    color = secondaryTextColor
                )
                Text(
                    text = complaint.date,
                    fontSize = 13.sp,
                    color = secondaryTextColor
                )
            }
        }
    }
}

data class Complaint(val title: String, val status: String, val date: String)

// ============ SETTINGS & PRIVACY SCREEN ============
@Composable
fun SettingsPrivacyScreen(navController: NavController, isDarkMode: Boolean) {
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(28.dp),
                tint = Color(0xFF1A237E)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Settings & Privacy",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Edit Profile Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Edit Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Username Field
                Text(
                    text = "Username",
                    fontSize = 13.sp,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = "Alex Johnson",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Field
                Text(
                    text = "Email",
                    fontSize = 13.sp,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = "alex.johnson@email.com",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Update Button
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A237E)
                    )
                ) {
                    Text("Update Profile", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Change Password Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Change Password",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Old Password
                Text(
                    text = "Old Password",
                    fontSize = 13.sp,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("Enter old password") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // New Password
                Text(
                    text = "New Password",
                    fontSize = 13.sp,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("Enter new password") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Change Password Button
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A237E)
                    )
                ) {
                    Text("Change Password", color = Color.White)
                }
            }
        }
    }
}

// ============ HELP & SUPPORT SCREEN ============
@Composable
fun HelpSupportScreen(navController: NavController, isDarkMode: Boolean) {
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(28.dp),
                tint = Color(0xFF1A237E)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Help & Support",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Terms & Conditions
        HelpCard(
            title = "Terms & Conditions",
            content = """
                By using SmartCity, you agree to our terms and conditions:
                
                • SmartCity is a civic engagement platform for reporting city issues
                • All complaints must be factual and respectful
                • Abusive or false reports will result in account suspension
                • Personal data is protected under privacy laws
                • SmartCity is not responsible for third-party actions
            """,
            cardColor = cardColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        // How to Use the App
        HelpCard(
            title = "How to Use SmartCity",
            content = """
                1. Create an Account
                   Create a profile with your name and email
                
                2. Report Issues
                   Click on "My Complaints" and submit new city issues
                   Provide location and detailed description
                
                3. Track Progress
                   View the status of your complaints:
                   - Open: Recently submitted
                   - In Progress: Being reviewed
                   - Resolved: Issue fixed
                
                4. Earn Recognition
                   • Get upvotes for helpful reports
                   • Become a Top Contributor
                   • Track your impact on the community
                
                5. Privacy & Settings
                   • Manage your profile information
                   • Change password anytime
                   • Control notification preferences
            """,
            cardColor = cardColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Reporting Guidelines
        HelpCard(
            title = "Reporting Guidelines",
            content = """
                When reporting an issue:
                
                ✓ Be specific - Include exact location and details
                ✓ Be respectful - Use professional language
                ✓ Provide context - Explain why it's a problem
                ✓ One issue per report - Keep reports focused
                ✓ Include evidence - Photos/videos if possible
                
                ✗ Don't report - Personal disputes or gossip
                ✗ Don't spam - Avoid duplicate complaints
                ✗ Don't be abusive - Keep tone respectful
            """,
            cardColor = cardColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun HelpCard(
    title: String,
    content: String,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                color = secondaryTextColor,
                lineHeight = 20.sp
            )
        }
    }
}

// ============ COMPOSABLE FUNCTIONS ============

@Composable
fun NavItem(icon: Any, label: String, isSelected: Boolean) {
    val color = if (isSelected) Color(0xFF1A237E) else Color.Gray
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when (icon) {
            is ImageVector -> Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            is Painter -> Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
        }
        Text(
            label,
            fontSize = 10.sp,
            color = color,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun StatItem(count: String, label: String, countColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = TextStyle(
                fontSize = 24.sp,
                color = countColor,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Text(
            text = label,
            style = TextStyle(
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun MenuRow(
    icon: Any,
    title: String,
    iconContainerColor: Color,
    iconColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconContainerColor, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (icon) {
                is ImageVector -> Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )

                is Painter -> Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.LightGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserprofilePreview() {
    var isDarkMode by remember { mutableStateOf(false) }
    UserprofileBody(
        navController = rememberNavController(),
        isDarkMode = isDarkMode,
        onDarkModeToggle = { isDarkMode = it }
    )
}