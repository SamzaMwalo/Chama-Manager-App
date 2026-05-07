package com.chamamanager104.ui.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamamanager104.core.data.DashboardRepository
import com.chamamanager104.core.model.DashboardSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val summary: DashboardSummary = DashboardSummary()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: DashboardRepository
) : ViewModel() {
    val state: StateFlow<DashboardUiState> = repository.observeSummary()
        .map { DashboardUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())
}
