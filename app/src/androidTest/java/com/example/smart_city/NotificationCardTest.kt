package com.example.smart_city

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.smart_city.model.Notification
import com.example.smart_city.ui.theme.SmartCityTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun notificationCard_displaysTitleAndMessage() {
        val notification = Notification(
            notificationId = "notification-1",
            title = "Complaint Resolved",
            message = "Your reported complaint has been resolved.",
            createdAt = 1_700_000_000_000
        )

        composeRule.setContent {
            SmartCityTheme {
                NotificationCard(
                    notification = notification,
                    isDarkMode = false
                )
            }
        }

        composeRule
            .onNodeWithText("Complaint Resolved")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(
                "Your reported complaint has been resolved."
            )
            .assertIsDisplayed()
    }
}