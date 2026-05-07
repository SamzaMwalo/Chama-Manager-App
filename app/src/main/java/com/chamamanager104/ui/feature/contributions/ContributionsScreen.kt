package com.chamamanager104.ui.feature.contributions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chamamanager104.ui.SectionColumn

@Composable
fun ContributionsScreen(
    state: ContributionsUiState,
    onRecord: (String, String, String) -> Unit
) {
    var memberId by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var dueOn by rememberSaveable { mutableStateOf("2026-05-31") }

    SectionColumn {
        Text("Contributions", style = MaterialTheme.typography.headlineMedium)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = memberId, onValueChange = { memberId = it }, label = { Text("Member ID") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount (KES)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = dueOn, onValueChange = { dueOn = it }, label = { Text("Due date") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { onRecord(memberId, amount, dueOn) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Record contribution")
                }
                Text("For production, wire the M-Pesa STK push button to a secure backend token flow.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.contributions) { contribution ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Member ${contribution.memberId}", style = MaterialTheme.typography.titleLarge)
                        Text("KES ${"%.2f".format(contribution.amount)}")
                        Text("Paid on ${contribution.paidOn}")
                        Text("Due on ${contribution.dueOn}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
