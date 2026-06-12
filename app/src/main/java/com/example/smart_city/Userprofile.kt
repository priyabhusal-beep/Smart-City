package com.example.smart_city

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.viewmodel.AuthViewModel
import com.example.smart_city.viewmodel.AuthViewModelFactory
import com.example.smart_city.repo.AuthRepository

class Userprofile : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserprofileApp(authViewModel = authViewModel)
        }
    }
}

// ✅ ADD THIS: Function to load current user when profile opens
@Composable
fun LoadUserForProfileEffect(authViewModel: AuthViewModel?) {
    LaunchedEffect(Unit) {
        try {
            val authRepository = AuthRepository()
            val currentUser = authRepository.getCurrentUser()
            authViewModel?.setCurrentUser(currentUser)
            Log.d("Userprofile", "User loaded: ${currentUser.name}")
        } catch (e: Exception) {
            Log.e("Userprofile", "Failed to load user: ${e.message}")
        }
    }
}

@Composable
fun UserprofileApp(authViewModel: AuthViewModel? = null) {
    // ✅ ADD THIS: Load user when profile opens
    LoadUserForProfileEffect(authViewModel)

    var isDarkMode by remember { mutableStateOf(false) }

    val lightBackground = Color(0xFFF8F9FA)
    val darkBackground = Color(0xFF121212)
    val lightText = Color.Black
    val darkText = Color(0xFFE0E0E0)

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
            // ✅ FIXED: Pass authViewModel to UserprofileBody
            UserprofileBody(
                isDarkMode,
                onDarkModeToggle = { isDarkMode = it },
                authViewModel = authViewModel  // ← ADD THIS
            )
        }
    }
}

@Composable
fun UserprofileBody(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    authViewModel: AuthViewModel? = null
) {
    // ✅ COLLECT current user from ViewModel
    val currentUser by authViewModel?.currentUser?.collectAsState()
        ?: remember { mutableStateOf(null) }

    val scrollState = rememberScrollState()
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    val context = LocalContext.current

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
                    contentDescription = "Notifications",
                    tint = Color(0xFF1A237E),
                    modifier = Modifier.size(24.dp)
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

                // ✅ SHOWS ACTUAL USER NAME
                Text(
                    text = currentUser?.name ?: "User",
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
                    // My Complaints
                    MenuRowNavigate(
                        icon = painterResource(R.drawable.baseline_report_24),
                        title = "My Complaints",
                        iconContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        iconColor = secondaryTextColor,
                        textColor = textColor,
                        onClick = {
                            // TODO: Navigate to My Complaints screen
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
                    )


                    // Settings & Privacy
                    MenuRowNavigate(
                        icon = painterResource(R.drawable.baseline_settings_24),
                        title = "Settings & Privacy",
                        iconContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        iconColor = secondaryTextColor,
                        textColor = textColor,
                        onClick = {
                            val intent = Intent(context, SettingPrivacyActivity::class.java)
                            context.startActivity(intent)
                        }
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
                                contentDescription = "Dark Mode",
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

                    // Help & Support
                    MenuRowNavigate(
                        icon = painterResource(R.drawable.baseline_help_24),
                        title = "Help & Support",
                        iconContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        iconColor = secondaryTextColor,
                        textColor = textColor,
                        onClick = {
                            val intent = Intent(context, HelpSupportActivity::class.java)
                            context.startActivity(intent)
                        }
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
fun MenuRowNavigate(
    icon: Painter,
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
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
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
        isDarkMode = isDarkMode,
        onDarkModeToggle = { isDarkMode = it }
    )
}