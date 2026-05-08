package com.chamamanager104.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamamanager104.core.common.ResultState
import com.chamamanager104.core.data.AuthRepository
import com.chamamanager104.core.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val result: ResultState<String> = ResultState.Idle
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _state.value = _state.value.copy(result = ResultState.Error("Email and password are required"))
                return@launch
            }
            _state.value = _state.value.copy(result = ResultState.Loading)
            val result = authRepository.signIn(email.trim(), password)
            _state.value = result.fold(
                onSuccess = { _state.value.copy(result = ResultState.Success("Welcome back ${it.fullName}")) },
                onFailure = { _state.value.copy(result = ResultState.Error(it.message ?: "Unable to login", it)) }
            )
        }
    }

    fun signup(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: UserRole,
        chamaName: String,
        inviteCode: String
    ) {
        viewModelScope.launch {
            if (fullName.isBlank() || phoneNumber.isBlank() || email.isBlank() || password.isBlank()) {
                _state.value = _state.value.copy(
                    result = ResultState.Error("Full name, phone, email, and password are required")
                )
                return@launch
            }

//            /**
//             * XOR VALIDATION (STRICT DOMAIN RULE)
//             * Exactly one of these must be provided:
//             * - create chama (chamaName)
//             * - join chama (inviteCode)
//             */

            if (chamaName.isBlank() && inviteCode.isBlank()) {
                _state.value = _state.value.copy(
                    result = ResultState.Error(
                        "Choose either: create a chama OR join using an invite code"
                    )
                )
                return@launch
            }

            if (chamaName.isNotBlank() && inviteCode.isNotBlank()) {
                _state.value = _state.value.copy(
                    result = ResultState.Error(
                        "You cannot create AND join a chama at the same time"
                    )
                )
                return@launch
            }

            _state.value = _state.value.copy(result = ResultState.Loading)
            val result = authRepository.signUp(
                fullName = fullName.trim(),
                phoneNumber = phoneNumber.trim(),
                email = email.trim(),
                password = password,
                role = role,
                chamaName = chamaName.trim(),
                inviteCode = inviteCode.trim()
            )
            _state.value = result.fold(
                onSuccess = { _state.value.copy(result = ResultState.Success("Account created for ${it.chamaName}")) },
                onFailure = { _state.value.copy(result = ResultState.Error(it.message ?: "Unable to sign up", it)) }
            )
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            if (email.isBlank()) {
                _state.value = _state.value.copy(result = ResultState.Error("Enter your email address to receive a reset link"))
                return@launch
            }
            _state.value = _state.value.copy(result = ResultState.Loading)
            val result = authRepository.sendPasswordReset(email.trim())
            _state.value = result.fold(
                onSuccess = { _state.value.copy(result = ResultState.Success("Password reset email sent to $email")) },
                onFailure = { _state.value.copy(result = ResultState.Error(it.message ?: "Unable to send reset email", it)) }
            )
        }
    }
}
