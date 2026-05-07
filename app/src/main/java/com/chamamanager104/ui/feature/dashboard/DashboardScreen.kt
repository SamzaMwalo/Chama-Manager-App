package com.chamamanager104.ui.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chamamanager104.core.model.UserRole
import com.chamamanager104.core.model.canManageFinance
import com.chamamanager104.core.model.canManageMembers
import com.chamamanager104.core.model.displayName
import com.chamamanager104.ui.SectionColumn

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    role: UserRole,
    onSignOut: () -> Unit
) {
    val cards = listOf(
        "Total savings" to "KES ${"%.2f".format(state.summary.totalSavings)}",
        "Active loans" to "KES ${"%.2f".format(state.summary.activeLoans)}",
        "Outstanding" to "KES ${"%.2f".format(state.summary.outstandingBalance)}",
        "Active members" to state.summary.activeMembers.toString(),
        "Meetings" to state.summary.meetingCount.toString(),
        "Repayment rate" to "${"%.1f".format(state.summary.repaymentRate)}%"
    )

    SectionColumn {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
                Text(
                    if (state.summary.chamaName.isBlank()) "${role.displayName()} workspace" else state.summary.chamaName,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(onClick = onSignOut) { Text("Sign out") }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Role focus", style = MaterialTheme.typography.titleLarge)
                Text(
                    when {
                        role.canManageMembers() -> "You can manage members, publish meetings, approve actions, and export reports."
                        role.canManageFinance() -> "You can record contributions, monitor loans, and track repayment health."
                        else -> "You can follow your savings, meetings, notifications, and account activity."
                    }
                )
            }
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2
        ) {
            cards.forEach { card ->
                Card(
                    modifier = Modifier.fillMaxWidth(0.48f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(card.first, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(card.second, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Operational insight", style = MaterialTheme.typography.titleLarge)
                        Text(
                            when {
                                role.canManageMembers() -> "Use this panel for attendance trends, approvals, member growth, and strategic investment review."
                                role.canManageFinance() -> "Use this panel for collection momentum, overdue balances, and loan-book health."
                                else -> "Use this panel for your savings streak, meeting readiness, and repayment reminders."
                            }
                        )
                        Text("Add Compose charts here after Firebase data is flowing live.")
                    }
                }
            }
        }
    }
}
