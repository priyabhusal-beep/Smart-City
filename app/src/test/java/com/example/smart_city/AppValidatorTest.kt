package com.example.smart_city

import com.example.smart_city.utils.AppValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppValidatorTest {

    @Test
    fun login_withValidInputs_returnsNoError() {
        val result = AppValidator.validateLogin(
            email = "user@gmail.com",
            password = "123456"
        )

        assertNull(result)
    }

    @Test
    fun login_withEmptyEmail_returnsError() {
        val result = AppValidator.validateLogin(
            email = "",
            password = "123456"
        )

        assertEquals(
            "Email cannot be empty",
            result
        )
    }

    @Test
    fun login_withInvalidEmail_returnsError() {
        val result = AppValidator.validateLogin(
            email = "wrong-email",
            password = "123456"
        )

        assertEquals(
            "Invalid email format",
            result
        )
    }

    @Test
    fun login_withShortPassword_returnsError() {
        val result = AppValidator.validateLogin(
            email = "user@gmail.com",
            password = "123"
        )

        assertEquals(
            "Password must be at least 6 characters",
            result
        )
    }

    @Test
    fun registration_withValidInputs_returnsNoError() {
        val result = AppValidator.validateRegistration(
            name = "Priya",
            email = "priya@gmail.com",
            password = "123456",
            confirmPassword = "123456"
        )

        assertNull(result)
    }

    @Test
    fun registration_withEmptyName_returnsError() {
        val result = AppValidator.validateRegistration(
            name = "",
            email = "priya@gmail.com",
            password = "123456",
            confirmPassword = "123456"
        )

        assertEquals(
            "Name cannot be empty",
            result
        )
    }

    @Test
    fun registration_withDifferentPasswords_returnsError() {
        val result = AppValidator.validateRegistration(
            name = "Priya",
            email = "priya@gmail.com",
            password = "123456",
            confirmPassword = "654321"
        )

        assertEquals(
            "Passwords do not match",
            result
        )
    }

    @Test
    fun complaintStatus_inProgress_isNormalizedCorrectly() {
        val result =
            AppValidator.normalizeComplaintStatus("In Progress")

        assertEquals(
            "in_progress",
            result
        )
    }

    @Test
    fun complaintStatus_completed_isNormalizedAsResolved() {
        val result =
            AppValidator.normalizeComplaintStatus("Completed")

        assertEquals(
            "resolved",
            result
        )
    }

    @Test
    fun complaintStatus_unknownValue_returnsUnknown() {
        val result =
            AppValidator.normalizeComplaintStatus("Rejected")

        assertEquals(
            "unknown",
            result
        )
    }
}