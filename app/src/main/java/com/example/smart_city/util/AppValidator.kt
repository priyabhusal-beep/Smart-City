package com.example.smart_city.utils

object AppValidator {

    fun validateLogin(
        email: String,
        password: String
    ): String? {
        return when {
            email.isBlank() ->
                "Email cannot be empty"

            !isValidEmail(email) ->
                "Invalid email format"

            password.isBlank() ->
                "Password cannot be empty"

            password.length < 6 ->
                "Password must be at least 6 characters"

            else -> null
        }
    }

    fun validateRegistration(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        return when {
            name.isBlank() ->
                "Name cannot be empty"

            email.isBlank() ->
                "Email cannot be empty"

            !isValidEmail(email) ->
                "Invalid email format"

            password.isBlank() ->
                "Password cannot be empty"

            password.length < 6 ->
                "Password must be at least 6 characters"

            password != confirmPassword ->
                "Passwords do not match"

            else -> null
        }
    }

    fun isValidEmail(email: String): Boolean {
        return email.matches(
            Regex(
                "^[A-Za-z0-9+_.-]+@" +
                        "[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )
        )
    }

    fun normalizeComplaintStatus(status: String): String {
        val normalized = status
            .trim()
            .lowercase()
            .replace("_", " ")
            .replace("-", " ")
            .replace(Regex("\\s+"), " ")

        return when (normalized) {
            "pending" -> "pending"

            "processing",
            "in progress",
            "inprogress",
            "working" -> "in_progress"

            "resolved",
            "completed",
            "complete" -> "resolved"

            else -> "unknown"
        }
    }
}