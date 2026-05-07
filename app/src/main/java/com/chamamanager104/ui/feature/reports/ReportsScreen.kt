package com.chamamanager104.ui.feature.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chamamanager104.ui.SectionColumn

@Composable
fun ReportsScreen() {
    SectionColumn {
        Text("Reports & Export", style = MaterialTheme.typography.headlineMedium)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Export member statements, savings reports, and financial summaries.", style = MaterialTheme.typography.titleLarge)
                Text("The report exporter is ready for CSV output. PDF generation can be added using Android PdfDocument or a backend service.")
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("Export members CSV")
                }
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("Export statement PDF")
                }
            }
        }
    }
}
