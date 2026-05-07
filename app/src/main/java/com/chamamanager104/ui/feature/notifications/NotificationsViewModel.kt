package com.chamamanager104.ui.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamamanager104.core.data.NotificationRepository
import com.chamamanager104.core.model.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class NotificationsUiState(
    val items: List<NotificationItem> = emptyList()
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    repository: NotificationRepository
) : ViewModel() {
    val state: StateFlow<NotificationsUiState> = repository.observeNotifications()
        .map { NotificationsUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NotificationsUiState())
}
