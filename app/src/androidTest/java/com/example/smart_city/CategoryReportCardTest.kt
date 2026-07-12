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
class CategoryReportCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun categoryCard_displaysCategoryAndCount() {
        composeRule.setContent {
            SmartCityTheme {
                CategoryReportSummaryCard(
                    category = "Road",
                    reportCount = 3,
                    isDarkMode = false,
                    onClick = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Road")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("3 Reports")
            .assertIsDisplayed()
    }

    @Test
    fun categoryCard_usesSingularReportText() {
        composeRule.setContent {
            SmartCityTheme {
                CategoryReportSummaryCard(
                    category = "Garbage",
                    reportCount = 1,
                    isDarkMode = false,
                    onClick = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Garbage")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("1 Report")
            .assertIsDisplayed()
    }
}