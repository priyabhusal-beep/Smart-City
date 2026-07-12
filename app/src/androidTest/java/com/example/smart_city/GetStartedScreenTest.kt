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
class GetStartedScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun getStartedScreen_displaysMainContent() {
        composeRule.setContent {
            SmartCityTheme {
                GetStartedContent()
            }
        }

        composeRule
            .onNodeWithText("SmartCity")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Report Issues Easily")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Skip")
            .assertIsDisplayed()
    }

    @Test
    fun getStartedScreen_displaysGetStartedButton() {
        composeRule.setContent {
            SmartCityTheme {
                GetStartedContent()
            }
        }

        composeRule
            .onNodeWithText("Get Started")
            .assertIsDisplayed()
    }
}