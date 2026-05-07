package com.chamamanager104.ui.feature.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamamanager104.core.data.MeetingRepository
import com.chamamanager104.core.model.Meeting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MeetingsUiState(
    val meetings: List<Meeting> = emptyList()
)

@HiltViewModel
class MeetingsViewModel @Inject constructor(
    private val repository: MeetingRepository
) : ViewModel() {

    val state: StateFlow<MeetingsUiState> = repository.observeMeetings()
        .map { MeetingsUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MeetingsUiState())

    fun saveMeeting(
        title: String,
        agenda: String,
        scheduledAt: String,
        venue: String,
        minutes: String,
        attendeesPresent: String,
        expectedAttendees: String
    ) {
        viewModelScope.launch {
            repository.saveMeeting(
                Meeting(
                    title = title,
                    agenda = agenda,
                    scheduledAt = scheduledAt,
                    venue = venue,
                    minutes = minutes,
                    attendeesPresent = attendeesPresent.toIntOrNull() ?: 0,
                    expectedAttendees = expectedAttendees.toIntOrNull() ?: 0
                )
            )
        }
    }
}
