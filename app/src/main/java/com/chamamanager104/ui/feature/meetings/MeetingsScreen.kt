//package com.chamamanager104.ui.feature.meetings
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.chamamanager104.ui.SectionColumn
//
//@Composable
//fun MeetingsScreen(
//    state: MeetingsUiState,
//    onSave: (String, String, String, String, String, String, String) -> Unit
//) {
//    var title by rememberSaveable { mutableStateOf("") }
//    var agenda by rememberSaveable { mutableStateOf("") }
//    var scheduledAt by rememberSaveable { mutableStateOf("2026-05-10 10:00") }
//    var venue by rememberSaveable { mutableStateOf("") }
//    var minutes by rememberSaveable { mutableStateOf("") }
//    var present by rememberSaveable { mutableStateOf("0") }
//    var expected by rememberSaveable { mutableStateOf("0") }
//
//    SectionColumn {
//        Text("Meetings", style = MaterialTheme.typography.headlineMedium)
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//        ) {
//            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
//                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Meeting title") }, modifier = Modifier.fillMaxWidth())
//                OutlinedTextField(value = agenda, onValueChange = { agenda = it }, label = { Text("Agenda") }, modifier = Modifier.fillMaxWidth())
//                OutlinedTextField(value = scheduledAt, onValueChange = { scheduledAt = it }, label = { Text("Scheduled at") }, modifier = Modifier.fillMaxWidth())
//                OutlinedTextField(value = venue, onValueChange = { venue = it }, label = { Text("Venue") }, modifier = Modifier.fillMaxWidth())
//                OutlinedTextField(value = minutes, onValueChange = { minutes = it }, label = { Text("Minutes") }, modifier = Modifier.fillMaxWidth())
//                OutlinedTextField(value = present, onValueChange = { present = it }, label = { Text("Attendees present") }, modifier = Modifier.fillMaxWidth())
//                OutlinedTextField(value = expected, onValueChange = { expected = it }, label = { Text("Expected attendees") }, modifier = Modifier.fillMaxWidth())
//                Button(onClick = { onSave(title, agenda, scheduledAt, venue, minutes, present, expected) }, modifier = Modifier.fillMaxWidth()) {
//                    Text("Save meeting")
//                }
//            }
//        }
//
//        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            items(state.meetings) { meeting ->
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
//                        Text(meeting.title, style = MaterialTheme.typography.titleLarge)
//                        Text(meeting.scheduledAt)
//                        Text(meeting.venue)
//                        Text("Attendance: ${meeting.attendeesPresent}/${meeting.expectedAttendees}", color = MaterialTheme.colorScheme.onSurfaceVariant)
//                    }
//                }
//            }
//        }
//    }
//}

package com.chamamanager104.ui.feature.meetings

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

@Composable
fun MeetingsScreen(
    state: MeetingsUiState,
    onSave: (String, String, String, String, String, String, String) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var agenda by rememberSaveable { mutableStateOf("") }
    var scheduledAt by rememberSaveable { mutableStateOf("2026-05-10 10:00") }
    var venue by rememberSaveable { mutableStateOf("") }
    var minutes by rememberSaveable { mutableStateOf("") }
    var present by rememberSaveable { mutableStateOf("0") }
    var expected by rememberSaveable { mutableStateOf("0") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Meetings", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Meeting title") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = agenda, onValueChange = { agenda = it }, label = { Text("Agenda") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = scheduledAt, onValueChange = { scheduledAt = it }, label = { Text("Scheduled at") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = venue, onValueChange = { venue = it }, label = { Text("Venue") }, maxLines = 1, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = minutes, onValueChange = { minutes = it }, label = { Text("Minutes") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = present, onValueChange = { present = it }, label = { Text("Attendees present") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = expected, onValueChange = { expected = it }, label = { Text("Expected attendees") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = { onSave(title, agenda, scheduledAt, venue, minutes, present, expected) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Save meeting")
                    }
                }
            }
        }

        items(state.meetings) { meeting ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(meeting.title, style = MaterialTheme.typography.titleLarge)
                    Text(meeting.scheduledAt)
                    Text(meeting.venue)
                    Text("Attendance: ${meeting.attendeesPresent}/${meeting.expectedAttendees}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}