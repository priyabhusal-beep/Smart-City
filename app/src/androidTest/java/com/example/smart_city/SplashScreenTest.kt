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
class SplashScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun splashScreen_displaysApplicationName() {
        composeRule.setContent {
            SmartCityTheme {
                SplashScreenContent()
            }
        }

        composeRule
            .onNodeWithText("SmartCity")
            .assertIsDisplayed()
    }

    @Test
    fun splashScreen_displaysTagline() {
        composeRule.setContent {
            SmartCityTheme {
                SplashScreenContent()
            }
        }

        composeRule
            .onNodeWithText("THE FUTURE OF LIVING")
            .assertIsDisplayed()
    }

    @Test
    fun splashScreen_displaysConnectionMessage() {
        composeRule.setContent {
            SmartCityTheme {
                SplashScreenContent()
            }
        }

        composeRule
            .onNodeWithText("Connecting to secure network...")
            .assertIsDisplayed()
    }
}