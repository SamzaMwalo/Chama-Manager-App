package com.chamamanager104.ui.feature.members

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chamamanager104.R
import com.chamamanager104.core.model.UserRole
import com.chamamanager104.core.model.canManageMembers
import com.chamamanager104.core.model.displayName

@Composable
fun MembersScreen(
    state: MembersUiState,
    role: UserRole,
    onSave: (String?, String, String, String) -> Unit,
    onDelete: (String) -> Unit
) {
    var editingMemberId by rememberSaveable { mutableStateOf<String?>(null) }
    var fullName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Members", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Signed in as ${role.displayName()}", style = MaterialTheme.typography.titleLarge)
                    Text(
                        if (role.canManageMembers()) {
                            "Chairperson can add, edit, and remove members, then monitor their contribution and loan history."
                        } else {
                            "This screen is read-only for your role. Chairperson handles member administration."
                        }
                    )
                }
            }
        }

        if (role.canManageMembers()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            if (editingMemberId == null) "Add member" else "Edit member",
                            style = MaterialTheme.typography.titleLarge
                        )

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.user_icon), contentDescription = "Member name", tint = MaterialTheme.colorScheme.primary) },
                            label = { Text("Full name") },
                            maxLines = 1,
                            shape = RoundedCornerShape(27.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.phone_icon), contentDescription = "Phone Number", tint = MaterialTheme.colorScheme.primary) },
                            label = { Text("Phone number") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            maxLines = 1,
                            shape = RoundedCornerShape(27.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            label = { Text("Email") },
                            maxLines = 1,
                            shape = RoundedCornerShape(27.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                onSave(editingMemberId, fullName, phone, email)
                                editingMemberId = null
                                fullName = ""
                                phone = ""
                                email = ""
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (editingMemberId == null) "Add member" else "Save changes")
                        }
                        if (editingMemberId != null) {
                            TextButton(
                                onClick = {
                                    editingMemberId = null
                                    fullName = ""
                                    phone = ""
                                    email = ""
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cancel editing")
                            }
                        }
                    }
                }
            }
        }

        items(state.members, key = { it.id }) { member ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(member.fullName, style = MaterialTheme.typography.titleLarge)
                        Text(member.phoneNumber, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(member.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    SelectionContainer {
                        Text(
                            "Member ID: ${member.id}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text("Contribution frequency: ${member.contributionFrequency.name}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Total contribution: KES ${"%.2f".format(member.totalContribution)}", color = MaterialTheme.colorScheme.primary)
                    Text("Outstanding loan: KES ${"%.2f".format(member.outstandingLoan)}", color = MaterialTheme.colorScheme.secondary)

                    if (role.canManageMembers()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    editingMemberId = member.id
                                    fullName = member.fullName
                                    phone = member.phoneNumber
                                    email = member.email
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Edit")
                            }

                            Button(
                                onClick = { onDelete(member.id) },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}

