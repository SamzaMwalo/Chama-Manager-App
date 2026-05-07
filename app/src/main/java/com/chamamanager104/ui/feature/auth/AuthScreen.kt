package com.chamamanager104.ui.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.chamamanager104.R
import com.chamamanager104.core.common.ResultState
import com.chamamanager104.core.model.UserRole
import com.chamamanager104.core.model.displayName

@Composable
private fun AuthCardShell(
    title: String,
    subtitle: String,
    state: AuthUiState,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Chama Manager", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
                Text(title, style = MaterialTheme.typography.headlineMedium)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (state.result is ResultState.Loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                content()
                when (val result = state.result) {
                    is ResultState.Error -> StatusBanner(
                        result.message,
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.colorScheme.onErrorContainer
                    )
                    is ResultState.Success -> StatusBanner(
                        result.data,
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun StatusBanner(
    message: String,
    containerColor: Color,
    textColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = containerColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(message, color = textColor, modifier = Modifier.padding(14.dp))
    }
}

@Composable
fun LoginScreen(
    state: AuthUiState,
    onLogin: (String, String) -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isLoading = state.result is ResultState.Loading
    var isVisible by remember { mutableStateOf(false) }

    AuthCardShell(
        title = "Welcome back",
        subtitle = "Sign in to manage your chama operations from one secure workspace.",
        state = state
    ) {
        OutlinedTextField(value = email, onValueChange = { email = it }, leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary) }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), maxLines = 1, modifier = Modifier.fillMaxWidth())///have added the keyboard options,
        OutlinedTextField(value = password, onValueChange = { password = it }, maxLines = 1,
            ///added
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.password_icon),
                    contentDescription = "Email",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            visualTransformation = if (isVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = { isVisible = !isVisible }
                ) {
                    if (isVisible) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.see_password),
                            contentDescription = "Password"
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.hide_password),
                            contentDescription = "Password"
                        )
                    }
                }
            },
            ///added
            label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        Button(
            onClick = { onLogin(email, password) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Signing in..." else "Login")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onNavigateToSignup) { Text("Create account") }
            TextButton(onClick = onNavigateToForgotPassword) { Text("Forgot password?") }
        }
    }
}

@Composable
fun SignupScreen(
    state: AuthUiState,
    onSignup: (String, String, String, String, UserRole, String, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var selectedRole by rememberSaveable { mutableStateOf(UserRole.MEMBER) }
    var chamaName by rememberSaveable { mutableStateOf("") }
    var inviteCode by rememberSaveable { mutableStateOf("") }
    val isLoading = state.result is ResultState.Loading
    var isVisible by remember { mutableStateOf(false) }
    val isCreatingChama = chamaName.isNotBlank() /// added later
    val isJoiningChama = inviteCode.isNotBlank() /// added later

    AuthCardShell(
        title = "Create account",
        subtitle = "Start a new chama or join an existing one with an invite code.",
        state = state
    ) {
        Text("Choose role", style = MaterialTheme.typography.titleLarge)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(UserRole.CHAIRPERSON, UserRole.TREASURER, UserRole.MEMBER).forEach { role ->
                FilterChip(
                    selected = selectedRole == role,
                    onClick = { selectedRole = role },
                    label = { Text(role.displayName()) }
                )
            }
        }
        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.user_icon), contentDescription = "User name", tint = MaterialTheme.colorScheme.primary) }, label = { Text("Full name") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = phone, onValueChange = { phone = it },
            ///added
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.phone_icon),
                    contentDescription = "Email",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            /// added
            label = { Text("Phone number") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary) }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), maxLines = 1, modifier = Modifier.fillMaxWidth()) ///added the keyboard options,
//        OutlinedTextField(value = chamaName, onValueChange = { chamaName = it }, leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.chamaname_icon), contentDescription = "Chama Name", tint = MaterialTheme.colorScheme.primary) }, label = { Text("New chama name") }, maxLines = 1, modifier = Modifier.fillMaxWidth()) /// replaced with below
        /// added
        OutlinedTextField(
            value = chamaName,
            onValueChange = {
                chamaName = it
                if (it.isNotBlank()) inviteCode = ""
            },
            enabled = inviteCode.isBlank(),
            leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.chamaname_icon), contentDescription = "Chama Name", tint = MaterialTheme.colorScheme.primary) },
            label = { Text("New chama name") },
            modifier = Modifier.fillMaxWidth()
        )
        /// added
//        OutlinedTextField(value = inviteCode, onValueChange = { inviteCode = it }, leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.invite_code_icon), contentDescription = "Invite code", tint = MaterialTheme.colorScheme.primary) }, label = { Text("Invite code to join") }, maxLines = 1, modifier = Modifier.fillMaxWidth()) /// replaced with below
        /// added
        OutlinedTextField(
            value = inviteCode,
            onValueChange = {
                inviteCode = it
                if (it.isNotBlank()) chamaName = ""
            },
            enabled = chamaName.isBlank(),
            leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.invite_code_icon), contentDescription = "Invite code", tint = MaterialTheme.colorScheme.primary) },
            label = { Text("Invite code to join") },
            modifier = Modifier.fillMaxWidth()
        )
        /// added
        OutlinedTextField(value = password, onValueChange = { password = it }, maxLines = 1,
            ///added
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.password_icon),
                    contentDescription = "Password",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            visualTransformation = if (isVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = { isVisible = !isVisible }
                ) {
                    if (isVisible) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.see_password),
                            contentDescription = "See Password Icon"
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.hide_password),
                            contentDescription = "Hide Password Icon"
                        )
                    }
                }
            },
            ///added,
            label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

        StatusBanner(
            message = when (selectedRole) {
                UserRole.CHAIRPERSON -> "Chairperson: member management, approvals, reports, and meeting publishing."
                UserRole.TREASURER -> "Treasurer: contributions, loans, repayments, and finance visibility."
                UserRole.MEMBER -> "Member: personal contributions, loan status, meetings, and alerts."
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            textColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StatusBanner(
            message = if (inviteCode.isBlank()) {
                "Leave invite code empty to create a new chama with the name above."
            } else {
                "Invite code entered. The app will join that chama and ignore the new chama name."
            },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            textColor = MaterialTheme.colorScheme.onTertiaryContainer
        )

        Button(
            onClick = { onSignup(fullName, phone, email, password, selectedRole, chamaName, inviteCode) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Creating account..." else "Create ${selectedRole.displayName()} account")
        }
        TextButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Back to login")
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    state: AuthUiState,
    onSendReset: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    val isLoading = state.result is ResultState.Loading

    AuthCardShell(
        title = "Forgot password",
        subtitle = "Enter your email and we will send a password reset link.",
        state = state
    ) {
        OutlinedTextField(value = email, onValueChange = { email = it }, leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary) }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())///added the keyboard options,
        Button(
            onClick = { onSendReset(email) },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Sending..." else "Send reset email")
        }
        TextButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Back to login")
        }
    }
}
