package com.example.smart_city

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.example.smart_city.viewmodel.AuthViewModel
import com.example.smart_city.viewmodel.AuthViewModelFactory
import com.example.smart_city.viewmodel.ImageViewModel
import com.example.smart_city.viewmodel.ReportViewModel
import java.io.ByteArrayOutputStream

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

@Composable
fun LoadUserForProfileEffect(authViewModel: AuthViewModel?) {
    LaunchedEffect(Unit) {
        authViewModel?.loadCurrentUserIfNeeded()
    }
}

@Composable
fun UserprofileApp(authViewModel: AuthViewModel? = null) {
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
            UserprofileBody(
                isDarkMode,
                onDarkModeToggle = { isDarkMode = it },
                authViewModel = authViewModel
            )
        }
    }
}

@Composable
fun UserprofileBody(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    authViewModel: AuthViewModel? = null,
    reportViewModel: ReportViewModel = viewModel(),
    imageViewModel: ImageViewModel = viewModel()
) {
    val currentUser by authViewModel?.currentUser?.collectAsState()
        ?: remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        reportViewModel.fetchUserComplaints()
    }

    val userComplaints = reportViewModel.userComplaints
    val totalReports = userComplaints.size
    val resolvedReports = userComplaints.count { it.status.lowercase() == "resolved" }

    val scrollState = rememberScrollState()
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF8F9FA)
    val cardColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color.Black
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    val context = LocalContext.current

    var showImageSourceDialog by remember { mutableStateOf(false) }
    var tempBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            tempBitmap = bitmap
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                "Profile_${System.currentTimeMillis()}",
                null
            )
            val uri = if (path != null) Uri.parse(path) else null
            if (uri != null) {
                imageViewModel.uploadImage(context, uri) { url ->
                    if (url != null) {
                        authViewModel?.updateProfilePicture(url)
                        tempBitmap = null
                        Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                    } else {
                        tempBitmap = null
                        Toast.makeText(context, "Upload failed, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                tempBitmap = null
                Toast.makeText(context, "Could not get image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // FIXED: gallery launcher — shows temp preview, uploads, saves to Firebase, updates StateFlow
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            return@rememberLauncherForActivityResult
        }

        // Show preview immediately
        try {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            tempBitmap = bitmap
        } catch (e: Exception) {
            Log.e("Gallery", "Failed to decode bitmap preview: ${e.message}")
        }

        Log.d("Gallery", "Starting upload for URI: $uri")
        imageViewModel.uploadImage(context, uri) { url ->
            tempBitmap = null
            if (url != null) {
                Log.d("Gallery", "Upload success, URL: $url")
                authViewModel?.updateProfilePicture(url)
                Toast.makeText(context, "Profile picture updated!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("Gallery", "Upload failed")
                Toast.makeText(context, "Upload failed, please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(containerColor = backgroundColor) { innerPadding ->
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
                            .clickable { showImageSourceDialog = true }
                    ) {
                        when {
                            tempBitmap != null -> {
                                Image(
                                    bitmap = tempBitmap!!.asImageBitmap(),
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            !currentUser?.profilePicture.isNullOrEmpty() -> {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(currentUser?.profilePicture)
                                        .diskCachePolicy(CachePolicy.DISABLED)
                                        .memoryCachePolicy(CachePolicy.DISABLED)
                                        .build(),
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.user),
                                    error = painterResource(R.drawable.user)
                                )
                            }
                            else -> {
                                Image(
                                    painter = painterResource(R.drawable.user),
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                        }
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(totalReports.toString(), "REPORTS", Color.Blue)
                StatItem("0", "UPVOTES", Color.Blue)
                StatItem(resolvedReports.toString(), "RESOLVED", Color(0xFF4CAF50))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    MenuRowNavigate(
                        icon = painterResource(R.drawable.baseline_report_24),
                        title = "My Complaints",
                        iconContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5),
                        iconColor = secondaryTextColor,
                        textColor = textColor,
                        onClick = {
                            val intent = Intent(context, AllUserComplain::class.java)
                            context.startActivity(intent)
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = if (isDarkMode) Color(0xFF333333) else Color(0xFFEEEEEE)
                    )

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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        authViewModel?.logout()
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    },
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

        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                title = { Text("Choose Profile Picture") },
                text = {
                    Column {
                        ListItem(
                            headlineContent = { Text("Camera") },
                            leadingContent = { Icon(Icons.Default.PhotoCamera, null) },
                            modifier = Modifier.clickable {
                                showImageSourceDialog = false
                                cameraLauncher.launch()
                            }
                        )
                        ListItem(
                            headlineContent = { Text("Gallery") },
                            leadingContent = { Icon(Icons.Default.PhotoLibrary, null) },
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