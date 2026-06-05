package com.example.smart_city

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smart_city.presentation.viewmodel.AuthViewModel
import com.example.smart_city.ui.theme.SmartCityTheme

class CreateAccount : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCityTheme {
                CreateAccountScreen(viewModel = authViewModel, activity = this)
            }
        }
    }
}

@Composable
fun CreateAccountScreen(viewModel: AuthViewModel, activity: Activity) {
    val context = LocalContext.current

    // State variables
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("user") }

    // Observe ViewModel state
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    // Show error messages
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF154385), Color(0xFF0B2D5E))
                    )
                )
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.smartcity),
                            contentDescription = "Smart City Logo",
                            modifier = Modifier.size(100.dp),
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Create Account",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Join the CitySmart ecosystem today.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CustomInputField(
                                "FULL NAME",
                                fullName,
                                { fullName = it },
                                Icons.Default.Person,
                                "John Doe",
                                enabled = !isLoading
                            )
                            CustomInputField(
                                "EMAIL",
                                email,
                                { email = it },
                                Icons.Default.Email,
                                "user@example.com",
                                KeyboardType.Email,
                                enabled = !isLoading
                            )
                            CustomInputField(
                                "PHONE NUMBER",
                                phone,
                                { phone = it },
                                Icons.Default.Phone,
                                "+977 9701234567",
                                KeyboardType.Phone,
                                enabled = !isLoading
                            )
                            CustomInputField(
                                "PASSWORD",
                                password,
                                { password = it },
                                Icons.Default.Lock,
                                "••••••••",
                                isPassword = true,
                                enabled = !isLoading
                            )
                            CustomInputField(
                                "CONFIRM PASSWORD",
                                confirmPassword,
                                { confirmPassword = it },
                                Icons.Default.Lock,
                                "••••••••",
                                isPassword = true,
                                enabled = !isLoading
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // User Type Selection
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Account Type:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(16.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = userType == "user",
                                        onClick = { userType = "user" },
                                        enabled = !isLoading
                                    )
                                    Text("User", fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = userType == "admin",
                                        onClick = { userType = "admin" },
                                        enabled = !isLoading
                                    )
                                    Text("Admin", fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Loading indicator
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color(0xFF005ED2)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // Sign Up Button
                            Button(
                                onClick = {
                                    // Validate
                                    if (fullName.isEmpty()) {
                                        Toast.makeText(context, "Please enter full name", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    if (email.isEmpty()) {
                                        Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    if (password.isEmpty()) {
                                        Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    if (password != confirmPassword) {
                                        Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    // Call register
                                    viewModel.register(
                                        email = email,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        name = fullName,
                                        userType = userType
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005ED2)),
                                enabled = !isLoading
                            ) {
                                Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                HorizontalDivider(
                                    modifier = Modifier.weight(1f),
                                    color = Color.LightGray.copy(alpha = 0.5f)
                                )
                                Text(
                                    "  OR CONTINUE WITH  ",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                                HorizontalDivider(
                                    modifier = Modifier.weight(1f),
                                    color = Color.LightGray.copy(alpha = 0.5f)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Google Sign-In coming soon", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.LightGray),
                                enabled = !isLoading
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.google),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Unspecified
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        "Continue with Google",
                                        color = Color.DarkGray,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(9.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 15.sp
                        )
                        TextButton(
                            onClick = {
                                val intent = android.content.Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                                activity.finish()
                            },
                            contentPadding = PaddingValues(0.dp),
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Login",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    enabled: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            leadingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.7f),
                focusedBorderColor = Color(0xFF005ED2)
            ),
            singleLine = true,
            enabled = enabled
        )
    }
}