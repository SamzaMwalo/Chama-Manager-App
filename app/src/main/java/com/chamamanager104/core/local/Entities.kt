package com.chamamanager104.core.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chamamanager104.core.model.ContributionFrequency
import com.chamamanager104.core.model.LoanStatus
import com.chamamanager104.core.model.UserRole

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey val id: String,
    val chamaId: String,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val nationalId: String,
    val joinDate: String,
    val status: String,
    val role: UserRole,
    val totalContribution: Double,
    val outstandingLoan: Double,
    val contributionFrequency: ContributionFrequency
)

@Entity(tableName = "contributions")
data class ContributionEntity(
    @PrimaryKey val id: String,
    val chamaId: String,
    val memberId: String,
    val amount: Double,
    val frequency: ContributionFrequency,
    val paidOn: String,
    val dueOn: String,
    val method: String,
    val receiptNumber: String,
    val confirmed: Boolean
)

@Entity(tableName = "loans")
data class LoanEntity(
    @PrimaryKey val id: String,
    val chamaId: String,
    val memberId: String,
    val principal: Double,
    val interestRate: Double,
    val durationMonths: Int,
    val requestedOn: String,
    val approvedOn: String,
    val dueDate: String,
    val status: LoanStatus,
    val penaltyRate: Double,
    val approvedBy: String,
    val notes: String
)

@Entity(tableName = "meetings")
data class MeetingEntity(
    @PrimaryKey val id: String,
    val chamaId: String,
    val title: String,
    val agenda: String,
    val scheduledAt: String,
    val venue: String,
    val minutes: String,
    val attendeesPresent: Int,
    val expectedAttendees: Int
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val chamaId: String,
    val title: String,
    val body: String,
    val type: String,
    val targetMemberId: String?,
    val createdAt: String,
    val read: Boolean
)
