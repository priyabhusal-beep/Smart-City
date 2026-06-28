package com.example.smart_city.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.smart_city.model.User
import com.example.smart_city.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel (application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()

    // Login State
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    // Register State
    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerState: StateFlow<RegisterUiState> = _registerState.asStateFlow()

    // Current Logged-in User
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Error Message
    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    // Loading State
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ================== LOGIN FUNCTION ==================
    fun login(email: String, password: String) {
        // Step 1: Validate inputs
        if (!validateLoginInputs(email, password)) {
            return
        }

        // Step 2: Start loading
        _loginState.value = LoginUiState.Loading
        _isLoading.value = true

        // Step 3: Call repository in background
        viewModelScope.launch {
            try {
                val user = authRepository.login(email, password)

                _currentUser.value = user
                _loginState.value = LoginUiState.Success(user)
                _isLoading.value = false

            } catch (e: Exception) {
                val errorMsg = e.message ?: "Login failed"
                _errorMessage.value = errorMsg
                _loginState.value = LoginUiState.Error(errorMsg)
                _isLoading.value = false
            }
        }
    }

    // ================== REGISTER FUNCTION ==================
    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        name: String,
        phone: String
    ) {
        if (!validateRegisterInputs(email, password, confirmPassword, name)) {
            return
        }

        _registerState.value = RegisterUiState.Loading
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val user = authRepository.register(
                    email = email,
                    password = password,
                    name = name,
                    phone = phone
                )

                _currentUser.value = user
                _registerState.value = RegisterUiState.Success(user)
                _errorMessage.value = "Registration successful! Please login."
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Registration failed"
                _errorMessage.value = errorMsg
                _registerState.value = RegisterUiState.Error(errorMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }
    // ================== GOOGLE SIGN-IN FUNCTION ==================
    fun signInWithGoogle(
        idToken: String,
        userType: String = "user"
    ) {
        _loginState.value = LoginUiState.Loading
        _registerState.value = RegisterUiState.Loading
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val user = authRepository.signInWithGoogle(
                    idToken = idToken,
                    userType = userType
                )

                _currentUser.value = user
                _loginState.value = LoginUiState.Success(user)
                _registerState.value = RegisterUiState.Success(user)
                _isLoading.value = false

            } catch (e: Exception) {
                val errorMsg = e.message ?: "Google Sign-In failed"
                _errorMessage.value = errorMsg
                _loginState.value = LoginUiState.Error(errorMsg)
                _registerState.value = RegisterUiState.Error(errorMsg)
                _isLoading.value = false
            }
        }
    }

    // ================== LOGOUT FUNCTION ==================
    fun logout() {
        authRepository.logout()
        _currentUser.value = null
        _loginState.value = LoginUiState.Idle
        _registerState.value = RegisterUiState.Idle
        _errorMessage.value = ""
    }
    fun setCurrentUser(user: User) {
        _currentUser.value = user
        Log.d("AuthVM", "Current user set to: ${user.name}")
    }

    // ================== VALIDATION FUNCTIONS ==================

    private fun validateLoginInputs(email: String, password: String): Boolean {
        when {
            email.isBlank() -> {
                _errorMessage.value = "Email cannot be empty"
                _loginState.value = LoginUiState.Error("Email cannot be empty")
                return false
            }
            !isValidEmail(email) -> {
                _errorMessage.value = "Invalid email format"
                _loginState.value = LoginUiState.Error("Invalid email format")
                return false
            }
            password.isBlank() -> {
                _errorMessage.value = "Password cannot be empty"
                _loginState.value = LoginUiState.Error("Password cannot be empty")
                return false
            }
            password.length < 6 -> {
                _errorMessage.value = "Password must be at least 6 characters"
                _loginState.value = LoginUiState.Error("Password must be at least 6 characters")
                return false
            }
        }
        return true
    }

    private fun validateRegisterInputs(
        email: String,
        password: String,
        confirmPassword: String,
        name: String
    ): Boolean {
        when {
            name.isBlank() -> {
                _errorMessage.value = "Name cannot be empty"
                return false
            }
            email.isBlank() -> {
                _errorMessage.value = "Email cannot be empty"
                return false
            }
            !isValidEmail(email) -> {
                _errorMessage.value = "Invalid email format"
                return false
            }
            password.isBlank() -> {
                _errorMessage.value = "Password cannot be empty"
                return false
            }
            password.length < 6 -> {
                _errorMessage.value = "Password must be at least 6 characters"
                return false
            }
            password != confirmPassword -> {
                _errorMessage.value = "Passwords do not match"
                return false
            }
        }
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }

    // ================== STATE MANAGEMENT ==================

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    fun resetLoginState() {
        _loginState.value = LoginUiState.Idle
    }

    fun resetRegisterState() {
        _registerState.value = RegisterUiState.Idle
    }
}

// ================== UI STATE CLASSES ==================

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}


sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val user: User) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class AuthViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(application) as T
    }
}