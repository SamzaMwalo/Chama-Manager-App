package com.chamamanager104.ui.feature.contributions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamamanager104.core.data.ContributionRepository
import com.chamamanager104.core.model.Contribution
import com.chamamanager104.core.model.ContributionFrequency
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ContributionsUiState(
    val contributions: List<Contribution> = emptyList()
)

@HiltViewModel
class ContributionsViewModel @Inject constructor(
    private val repository: ContributionRepository
) : ViewModel() {

    val state: StateFlow<ContributionsUiState> = repository.observeContributions()
        .map { ContributionsUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ContributionsUiState())

    fun recordContribution(memberId: String, amount: String, dueOn: String) {
        viewModelScope.launch {
            repository.recordContribution(
                Contribution(
                    memberId = memberId,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    frequency = ContributionFrequency.MONTHLY,
                    paidOn = "2026-05-01",
                    dueOn = dueOn,
                    confirmed = true
                )
            )
        }
    }
}
