package com.example.smart_city

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.smart_city.ui.theme.SmartCityTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForgotPasswordScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun forgotPasswordScreen_displaysRequiredElements() {
        composeRule.setContent {
            SmartCityTheme {
                ForgetPasswordScreen(
                    onBackClick = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Reset Password")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Forgot Password?")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Email Address")
            .assertIsDisplayed()
    }
}