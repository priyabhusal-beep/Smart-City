package com.example.smart_city

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.viewmodel.AuthViewModel
import com.example.smart_city.viewmodel.LoginUiState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smart_city.ui.theme.SmartCityTheme

class LoginActivity : ComponentActivity() {

    // Use the SHARED ViewModel from Application, not a new instance
    private val authViewModel: AuthViewModel by lazy {
        (application as SmartCityApplication).authViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (authViewModel.currentUser.value != null) {
            navigateToDashboard()
            finish()
            return
        }

        setContent {
            SmartCityTheme {
                LoginScreen(viewModel = authViewModel)
            }
        }
    }

    private fun navigateToDashboard() {
        val userType = authViewModel.currentUser.value?.userType
        val intent = when (userType) {
            "admin" -> Intent(this, AdminDashboard::class.java)
            else -> Intent(this, HomeScreen::class.java)
        }
        startActivity(intent)
        finish()
    }
}

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity

    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Success) {
            val userType = currentUser?.userType
            val intent = when (userType) {
                "admin" -> Intent(context, AdminDashboard::class.java)
                else -> Intent(context, HomeScreen::class.java)
            }
            context.startActivity(intent)
            activity?.finish()
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            android.widget.Toast.makeText(context, errorMessage, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF1F4F8)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.login),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                ) {
                    Text(text = "SmartCity", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                    Text(text = "Access your dashboard", fontSize = 14.sp, color = Color.Gray)
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 32.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("Email Address", color = Color.Gray) },
                            leadingIcon = { Icon(painter = painterResource(R.drawable.baseline_mail_24), contentDescription = null, tint = Color.Black) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFE5E7EB), focusedBorderColor = Color(0xFF1E3A8A)),
                            singleLine = true,
                            enabled = !isLoading,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Password", color = Color.Gray) },
                            leadingIcon = { Icon(painter = painterResource(R.drawable.baseline_lock_24), contentDescription = null, tint = Color.Black) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }, enabled = !isLoading) {
                                    Icon(
                                        painter = painterResource(id = if (passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFE5E7EB), focusedBorderColor = Color(0xFF1E3A8A)),
                            singleLine = true,
                            enabled = !isLoading
                        )

                        TextButton(
                            onClick = { android.widget.Toast.makeText(context, "Reset link will be sent to your email", android.widget.Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.align(Alignment.End),
                            contentPadding = PaddingValues(0.dp),
                            enabled = !isLoading
                        ) {
                            Text("FORGOT PASSWORD?", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(bottom = 8.dp), color = Color(0xFF1E3A8A))
                        }

                        Button(
                            onClick = {
                                if (email.isEmpty() || password.isEmpty()) {
                                    android.widget.Toast.makeText(context, "Please enter email and password", android.widget.Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.login(email, password)
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A), disabledContainerColor = Color(0xFF1E3A8A).copy(alpha = 0.5f)),
                            enabled = !isLoading
                        ) {
                            Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
                            Text("OR CONTINUE WITH", modifier = Modifier.padding(horizontal = 12.dp), fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        OutlinedButton(
                            onClick = { android.widget.Toast.makeText(context, "Google Sign-In coming soon", android.widget.Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            enabled = !isLoading
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google Logo", modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Google", color = Color.Black, fontWeight = FontWeight.Medium)
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Don't have an account? ", color = Color.Gray, fontSize = 14.sp)
                            TextButton(
                                onClick = {
                                    val intent = Intent(context, CreateAccount::class.java)
                                    context.startActivity(intent)
                                    activity?.finish()
                                },
                                contentPadding = PaddingValues(0.dp),
                                enabled = !isLoading
                            ) {
                                Text("Sign Up", color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    SmartCityTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF1F4F8)) {}
    }
}