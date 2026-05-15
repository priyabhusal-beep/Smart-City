package com.example.smart_city

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

    var email by remember { mutableStateOf("admin@citysmart.gov") }
    var password by remember { mutableStateOf("password123") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Background Image
        Image(
            painter = painterResource(id = R.drawable.backgroundimage), // add your image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA001133))
        )

        // Login Card
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(color = Color.White)
                    .padding(24.dp)
            ) {

                // Top Icon Box
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E90FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CitySmart Admin",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Secure Control Center Access",
                    fontSize = 13.sp,
                    color = Color(0xFF001B44)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Email Label
                Text(
                    text = "EMAIL ADDRESS",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x332BA7FF),
                        unfocusedContainerColor = Color(0x332BA7FF),
                        focusedBorderColor = Color(0xFF002B7A),
                        unfocusedBorderColor = Color(0xFF002B7A),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Password Label
                Text(
                    text = "PASSWORD",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null
                        )
                    },
                    visualTransformation =
                        if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),

                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x332BA7FF),
                        unfocusedContainerColor = Color(0x332BA7FF),
                        focusedBorderColor = Color(0xFF002B7A),
                        unfocusedBorderColor = Color(0xFF002B7A),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Remember + Forgot
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = {
                                rememberMe = it
                            }
                        )

                        Text(
                            text = "Remember me",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = "Forgot Access?",
                        fontSize = 12.sp,
                        color = Color(0xFF001B88),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Sign In Button
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF001A8F)
                    )
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "© ADMIN ACCESS ONLY",
                    fontSize = 11.sp,
                    color = Color(0xFF001B44)
                )
            }
        }

        // Bottom Footer
        Text(
            text = "© 2026 Urban Systems Authority. All rights reserved.",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            color = Color(0xFF80BFFF),
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    SmartCityTheme {
        AdminScreen()
    }
}