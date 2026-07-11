package com.example.smart_city

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smart_city.ui.theme.SmartCityTheme
import com.example.smart_city.viewmodel.AuthViewModel
import com.example.smart_city.viewmodel.RegisterUiState
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateAccount : ComponentActivity() {

    private val authViewModel: AuthViewModel by lazy {
        (application as SmartCityApplication).authViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartCityTheme {
                CreateAccountScreen(
                    viewModel = authViewModel,
                    activity = this
                )
            }
        }
    }
}

@Composable
fun CreateAccountScreen(
    viewModel: AuthViewModel,
    activity: Activity
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val registerState by viewModel.registerState.collectAsStateWithLifecycle()

    val credentialManager = remember { CredentialManager.create(context) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    // Show a success message first, then navigate to Login after a short delay
    LaunchedEffect(registerState) {
        if (registerState is RegisterUiState.Success) {
            Toast.makeText(
                context,
                "Account created successfully!",
                Toast.LENGTH_LONG
            ).show()

            delay(1500) // let the user see the toast before navigating

            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            activity.finish()
        }
    }

    fun startGoogleRegister() {
        coroutineScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(
                        "370184886750-dmmpsqps6mih9equadgiu8fqu6rpesc0.apps.googleusercontent.com"
                    )
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                val googleCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)

                viewModel.signInWithGoogle(
                    idToken = googleCredential.idToken,
                    userType = "citizen"
                )

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    e.message ?: "Google registration failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                        colors = listOf(
                            Color(0xFF154385),
                            Color(0xFF0B2D5E)
                        )
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
                        border = BorderStroke(
                            1.dp,
                            Color.White.copy(alpha = 0.2f)
                        )
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFDFDFD)
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            CustomInputField(
                                label = "FULL NAME",
                                value = fullName,
                                onValueChange = { fullName = it },
                                icon = Icons.Default.Person,
                                placeholder = "John Doe",
                                enabled = !isLoading
                            )

                            CustomInputField(
                                label = "EMAIL",
                                value = email,
                                onValueChange = { email = it },
                                icon = Icons.Default.Email,
                                placeholder = "user@example.com",
                                keyboardType = KeyboardType.Email,
                                enabled = !isLoading
                            )

                            CustomInputField(
                                label = "PHONE NUMBER",
                                value = phone,
                                onValueChange = { phone = it },
                                icon = Icons.Default.Phone,
                                placeholder = "+977 9701234567",
                                keyboardType = KeyboardType.Phone,
                                enabled = !isLoading
                            )

                            CustomInputField(
                                label = "PASSWORD",
                                value = password,
                                onValueChange = { password = it },
                                icon = Icons.Default.Lock,
                                placeholder = "••••••••",
                                keyboardType = KeyboardType.Password,
                                isPassword = true,
                                enabled = !isLoading
                            )

                            CustomInputField(
                                label = "CONFIRM PASSWORD",
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                icon = Icons.Default.Lock,
                                placeholder = "••••••••",
                                keyboardType = KeyboardType.Password,
                                isPassword = true,
                                enabled = !isLoading
                            )

                            Spacer(modifier = Modifier.height(22.dp))

                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color(0xFF005ED2)
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Button(
                                onClick = {
                                    when {
                                        fullName.isBlank() -> {
                                            Toast.makeText(
                                                context,
                                                "Please enter full name",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }

                                        email.isBlank() -> {
                                            Toast.makeText(
                                                context,
                                                "Please enter email",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }

                                        phone.isBlank() -> {
                                            Toast.makeText(
                                                context,
                                                "Please enter phone number",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }

                                        password.isBlank() -> {
                                            Toast.makeText(
                                                context,
                                                "Please enter password",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }

                                        password != confirmPassword -> {
                                            Toast.makeText(
                                                context,
                                                "Passwords don't match",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }
                                    }

                                    viewModel.register(
                                        email = email,
                                        password = password,
                                        confirmPassword = confirmPassword,
                                        name = fullName,
                                        phone = phone
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF005ED2)
                                ),
                                enabled = !isLoading
                            ) {
                                Text(
                                    text = "Sign Up",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                HorizontalDivider(
                                    modifier = Modifier.weight(1f),
                                    color = Color.LightGray.copy(alpha = 0.5f)
                                )

                                Text(
                                    text = "  OR CONTINUE WITH  ",
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
                                    startGoogleRegister()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    1.dp,
                                    Color(0xFFC8C8C8)
                                ),
                                enabled = !isLoading
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.google),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Unspecified
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = "Continue with Google",
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
                                val intent = Intent(
                                    context,
                                    LoginActivity::class.java
                                )
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

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF666666),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF9E9E9E)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF6D6D6D),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (isPassword) {
                    val eyeIcon = if (passwordVisible) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Default.VisibilityOff
                    }

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = eyeIcon,
                            contentDescription = if (passwordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            },
                            tint = Color(0xFF6D6D6D),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF3F4F6),
                unfocusedContainerColor = Color(0xFFF3F4F6),
                disabledContainerColor = Color(0xFFF3F4F6),

                focusedBorderColor = Color(0xFFB5B5B5),
                unfocusedBorderColor = Color(0xFFC8C8C8),
                disabledBorderColor = Color(0xFFD0D0D0),

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.DarkGray,

                cursorColor = Color(0xFF005ED2)
            ),
            singleLine = true,
            enabled = enabled
        )
    }
}