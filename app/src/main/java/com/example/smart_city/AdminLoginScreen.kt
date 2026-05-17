package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_city.ui.theme.SmartCityTheme

class AdminLoginScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartCityTheme {
                AdminScreen()
            }
        }
    }
}

@Composable
fun AdminScreen() {

    var email by remember {
        mutableStateOf("admin@citysmart.gov")
    }

    var password by remember {
        mutableStateOf("shh_secret_pass")
    }

    var rememberMe by remember {
        mutableStateOf(false)
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF002561),
                        Color(0xFF003988),
                        Color(0xFF001E50)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(36.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE2E7EE)
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 32.dp
                    ),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // HEADER LOGO
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF1E3A6B)),

                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(
                                id = R.drawable.smartcity
                            ),
                            contentDescription = "Smart City Logo",

                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "CitySmart Admin",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Secure Control Center Access",
                        color = Color(0xFF4B5563),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // EMAIL TITLE
                    Text(
                        text = "EMAIL ADDRESS",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B7280),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // EMAIL FIELD
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        singleLine = true,

                        shape = RoundedCornerShape(12.dp),

                        leadingIcon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.baseline_mail_24
                                ),
                                contentDescription = "Email Icon",
                                tint = Color(0xFF9CA3AF)
                            )
                        },

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,

                            focusedBorderColor = Color(0xFFD1D5DB),
                            unfocusedBorderColor = Color(0xFFD1D5DB),

                            focusedTextColor = Color(0xFF1F2937),
                            unfocusedTextColor = Color(0xFF4B5563)
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // PASSWORD TITLE
                    Text(
                        text = "PASSWORD",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B7280),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // PASSWORD FIELD
                    OutlinedTextField(
                        value = password,

                        onValueChange = {
                            password = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        singleLine = true,

                        shape = RoundedCornerShape(12.dp),

                        leadingIcon = {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.baseline_lock_24
                                ),
                                contentDescription = "Lock Icon",
                                tint = Color(0xFF9CA3AF)
                            )
                        },

                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible =
                                        !passwordVisible
                                }
                            ) {

                                Icon(
                                    painter = painterResource(
                                        id =
                                            if (passwordVisible)
                                                R.drawable.baseline_visibility_off_24
                                            else
                                                R.drawable.baseline_visibility_24
                                    ),

                                    contentDescription =
                                        "Visibility Toggle",

                                    tint = Color(0xFF9CA3AF)
                                )
                            }
                        },

                        visualTransformation =
                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Password
                            ),

                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedContainerColor =
                                    Color.White,

                                unfocusedContainerColor =
                                    Color.White,

                                focusedBorderColor =
                                    Color(0xFFD1D5DB),

                                unfocusedBorderColor =
                                    Color(0xFFD1D5DB),

                                focusedTextColor =
                                    Color(0xFF1F2937),

                                unfocusedTextColor =
                                    Color(0xFF4B5563)
                            )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // OPTIONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Row(
                            modifier =
                                Modifier.weight(1f),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = rememberMe,

                                onCheckedChange = {
                                    rememberMe = it
                                },

                                colors =
                                    CheckboxDefaults.colors(
                                        checkedColor =
                                            Color(0xFF0056C6),

                                        uncheckedColor =
                                            Color(0xFF6B7280)
                                    )
                            )

                            Text(
                                text = "Remember device",
                                color = Color(0xFF4B5563),
                                fontSize = 13.sp
                            )
                        }

                        TextButton(
                            onClick = {},
                            contentPadding =
                                PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Forgot Access?",
                                color = Color(0xFF0056C6),
                                fontWeight =
                                    FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // SIGN IN BUTTON
                    Button(
                        onClick = {},

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),

                        shape =
                            RoundedCornerShape(12.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF0056C6)
                            )
                    ) {

                        Text(
                            text = "Sign In",
                            fontSize = 16.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // SECURITY INDICATOR
                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically,

                        horizontalArrangement =
                            Arrangement.Center
                    ) {

                        Icon(
                            painter = painterResource(
                                id = R.drawable.baseline_lock_24
                            ),

                            contentDescription =
                                "Shield",

                            tint = Color(0xFF6B7280),

                            modifier =
                                Modifier.size(14.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(4.dp)
                        )

                        Text(
                            text =
                                "ADMIN ACCESS ONLY",

                            color =
                                Color(0xFF6B7280),

                            fontWeight =
                                FontWeight.Bold,

                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            // FOOTER
            Row(
                modifier =
                    Modifier.padding(bottom = 8.dp),

                horizontalArrangement =
                    Arrangement.Center,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "Already have an account? ",

                    color =
                        Color.White.copy(
                            alpha = 0.7f
                        ),

                    fontSize = 14.sp
                )

                TextButton(
                    onClick = {},
                    contentPadding =
                        PaddingValues(0.dp)
                ) {

                    Text(
                        text = "Login",

                        color = Color.White,

                        fontWeight =
                            FontWeight.Bold,

                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AdminScreenPreview() {
    SmartCityTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            AdminScreen()
        }
    }
}