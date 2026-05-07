//package com.chamamanager104.ui.feature.members
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.chamamanager104.core.data.MemberRepository
//import com.chamamanager104.core.model.Member
//import com.chamamanager104.core.model.UserRole
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
//data class MembersUiState(
//    val members: List<Member> = emptyList()
//)
//
//@HiltViewModel
//class MembersViewModel @Inject constructor(
//    private val repository: MemberRepository
//) : ViewModel() {
//
//    val state: StateFlow<MembersUiState> = repository.observeMembers()
//        .map { MembersUiState(it) }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MembersUiState())
//
//    fun saveMember(fullName: String, phoneNumber: String, email: String) {
//        viewModelScope.launch {
//            repository.saveMember(
//                Member(
//                    fullName = fullName,
//                    phoneNumber = phoneNumber,
//                    email = email,
//                    nationalId = "",
//                    joinDate = "2026-05-01",
//                    role = UserRole.MEMBER
//                )
//            )
//        }
//    }
//
//    fun deleteMember(memberId: String) {
//        viewModelScope.launch {
//            repository.deleteMember(memberId)
//        }
//    }
//}

package com.chamamanager104.ui.feature.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chamamanager104.core.data.MemberRepository
import com.chamamanager104.core.model.Member
import com.chamamanager104.core.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MembersUiState(
    val members: List<Member> = emptyList()
)

@HiltViewModel
class MembersViewModel @Inject constructor(
    private val repository: MemberRepository
) : ViewModel() {

    val state: StateFlow<MembersUiState> = repository.observeMembers()
        .map { MembersUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MembersUiState())

    fun saveMember(memberId: String?, fullName: String, phoneNumber: String, email: String) {
        viewModelScope.launch {
            val existingMember = memberId?.let { id ->
                state.value.members.firstOrNull { it.id == id }
            }

            repository.saveMember(
                existingMember?.copy(
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    email = email
                ) ?: Member(
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    email = email,
                    nationalId = "",
                    joinDate = "2026-05-01",
                    role = UserRole.MEMBER
                )
            )
        }
    }

    fun deleteMember(memberId: String) {
        viewModelScope.launch {
            repository.deleteMember(memberId)
        }
    }
}
