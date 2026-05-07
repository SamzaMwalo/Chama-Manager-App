package com.chamamanager104.core.data

import com.chamamanager104.core.model.Contribution
import com.chamamanager104.core.model.DashboardSummary
import com.chamamanager104.core.model.Loan
import com.chamamanager104.core.model.Meeting
import com.chamamanager104.core.model.Member
import com.chamamanager104.core.model.NotificationItem
import com.chamamanager104.core.model.UserRole
import com.chamamanager104.core.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<UserSession?>
    suspend fun signIn(email: String, password: String): Result<UserSession>
    suspend fun signUp(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: UserRole,
        chamaName: String,
        inviteCode: String
    ): Result<UserSession>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun signOut()
}

interface MemberRepository {
    fun observeMembers(): Flow<List<Member>>
    suspend fun saveMember(member: Member): Result<Unit>
    suspend fun deleteMember(memberId: String): Result<Unit>
}

interface ContributionRepository {
    fun observeContributions(): Flow<List<Contribution>>
    fun observeMemberContributions(memberId: String): Flow<List<Contribution>>
    suspend fun recordContribution(contribution: Contribution): Result<Unit>
    suspend fun initiateMpesaPayment(member: Member, amount: Double): Result<String>
}

interface LoanRepository {
    fun observeLoans(): Flow<List<Loan>>
    suspend fun saveLoan(loan: Loan): Result<Unit>
}

interface MeetingRepository {
    fun observeMeetings(): Flow<List<Meeting>>
    suspend fun saveMeeting(meeting: Meeting): Result<Unit>
}

interface NotificationRepository {
    fun observeNotifications(): Flow<List<NotificationItem>>
}

interface DashboardRepository {
    fun observeSummary(): Flow<DashboardSummary>
}
