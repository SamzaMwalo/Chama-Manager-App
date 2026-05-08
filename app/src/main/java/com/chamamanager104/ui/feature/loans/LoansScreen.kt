package com.chamamanager104.ui.feature.loans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.chamamanager104.R

@Composable
fun LoansScreen(
    state: LoansUiState,
    onSave: (String, String, String, String, String) -> Unit
) {

    var memberId by rememberSaveable { mutableStateOf("") }
    var principal by rememberSaveable { mutableStateOf("") }
    var rate by rememberSaveable { mutableStateOf("10") }
    var duration by rememberSaveable { mutableStateOf("3") }
    var notes by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // HEADER
        item {
            Text(
                text = "Loans",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        // FORM CARD
        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedTextField(
                        value = memberId,
                        onValueChange = { memberId = it },
                        leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.userid_icon), contentDescription = "Member ID", tint = MaterialTheme.colorScheme.primary) },
                        label = { Text("Member ID") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    OutlinedTextField(
                        value = principal,
                        onValueChange = { principal = it },
                        leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.currency_icon), contentDescription = "Principal", tint = MaterialTheme.colorScheme.primary) },
                        label = { Text("Principal Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    OutlinedTextField(
                        value = rate,
                        onValueChange = { rate = it },
                        label = { Text("Interest Rate (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duration (Months)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        leadingIcon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.notes_icon), contentDescription = "Purpose of Loan", tint = MaterialTheme.colorScheme.primary) },
                        label = { Text("Purpose of Loan") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    Button(
                        onClick = {
                            onSave(
                                memberId,
                                principal,
                                rate,
                                duration,
                                notes
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit Loan")
                    }
                }
            }
        }

        // LOAN LIST
        items(state.loans) { loan ->

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = "Member ${loan.memberId}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Principal: KES ${"%.2f".format(loan.principal)}"
                    )

                    Text(
                        text = "Total Repayable: KES ${
                            "%.2f".format(loan.totalRepayable)
                        }"
                    )

                    Text(
                        text = "Status: ${loan.status.name}"
                    )
                }
            }
        }
    }
}