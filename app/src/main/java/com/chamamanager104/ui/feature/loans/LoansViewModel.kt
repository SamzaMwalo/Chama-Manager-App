package com.chamamanager104.ui.feature.loans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamamanager104.core.data.LoanRepository
import com.chamamanager104.core.model.Loan
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LoansUiState(
    val loans: List<Loan> = emptyList()
)

@HiltViewModel
class LoansViewModel @Inject constructor(
    private val repository: LoanRepository
) : ViewModel() {

    val state: StateFlow<LoansUiState> = repository.observeLoans()
        .map { LoansUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LoansUiState())

    fun saveLoan(memberId: String, principal: String, rate: String, duration: String, notes: String) {
        viewModelScope.launch {
            repository.saveLoan(
                Loan(
                    memberId = memberId,
                    principal = principal.toDoubleOrNull() ?: 0.0,
                    interestRate = rate.toDoubleOrNull() ?: 10.0,
                    durationMonths = duration.toIntOrNull() ?: 3,
                    requestedOn = "2026-05-01",
                    dueDate = "2026-08-01",
                    notes = notes
                )
            )
        }
    }
}
