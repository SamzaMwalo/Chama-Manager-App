package com.chamamanager104.ui.feature.loans

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
fun LoansScreen(
    state: LoansUiState,
    onSave: (String, String, String, String, String) -> Unit
) {
    var memberId by rememberSaveable { mutableStateOf("") }
    var principal by rememberSaveable { mutableStateOf("") }
    var rate by rememberSaveable { mutableStateOf("10") }
    var duration by rememberSaveable { mutableStateOf("3") }
    var notes by rememberSaveable { mutableStateOf("") }

    SectionColumn {
        Text("Loans", style = MaterialTheme.typography.headlineMedium)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = memberId, onValueChange = { memberId = it }, label = { Text("Member ID") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = principal, onValueChange = { principal = it }, label = { Text("Principal") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = rate, onValueChange = { rate = it }, label = { Text("Interest %") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration months") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
                Button(onClick = { onSave(memberId, principal, rate, duration, notes) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Submit loan")
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.loans) { loan ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Member ${loan.memberId}", style = MaterialTheme.typography.titleLarge)
                        Text("Principal: KES ${"%.2f".format(loan.principal)}")
                        Text("Total repayable: KES ${"%.2f".format(loan.totalRepayable)}")
                        Text("Status: ${loan.status.name}")
                    }
                }
            }
        }
    }
}
