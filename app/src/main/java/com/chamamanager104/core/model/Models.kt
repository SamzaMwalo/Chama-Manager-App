package com.chamamanager104.core.model

import kotlinx.serialization.Serializable

enum class UserRole {
    CHAIRPERSON,
    TREASURER,
    MEMBER
}

fun UserRole.canManageMembers(): Boolean = this == UserRole.CHAIRPERSON

fun UserRole.canManageFinance(): Boolean = this == UserRole.CHAIRPERSON || this == UserRole.TREASURER

fun UserRole.displayName(): String = when (this) {
    UserRole.CHAIRPERSON -> "Chairperson"
    UserRole.TREASURER -> "Treasurer"
    UserRole.MEMBER -> "Member"
}

enum class ContributionFrequency {
    WEEKLY,
    MONTHLY
}

enum class LoanStatus {
    PENDING,
    APPROVED,
    REJECTED,
    ACTIVE,
    COMPLETED,
    OVERDUE
}

@Serializable
data class Chama(
    val id: String = "",
    val name: String = "",
    val inviteCode: String = "",
    val description: String = ""
)

@Serializable
data class UserSession(
    val uid: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val role: UserRole = UserRole.MEMBER,
    val chamaId: String = "",
    val chamaName: String = "",
    val chamaCode: String = ""
)

@Serializable
data class Member(
    val id: String = "",
    val chamaId: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val nationalId: String = "",
    val joinDate: String = "",
    val status: String = "ACTIVE",
    val role: UserRole = UserRole.MEMBER,
    val totalContribution: Double = 0.0,
    val outstandingLoan: Double = 0.0,
    val contributionFrequency: ContributionFrequency = ContributionFrequency.MONTHLY
)

@Serializable
data class Contribution(
    val id: String = "",
    val chamaId: String = "",
    val memberId: String = "",
    val amount: Double = 0.0,
    val frequency: ContributionFrequency = ContributionFrequency.MONTHLY,
    val paidOn: String = "",
    val dueOn: String = "",
    val method: String = "M-PESA",
    val receiptNumber: String = "",
    val confirmed: Boolean = false
)

@Serializable
data class Loan(
    val id: String = "",
    val chamaId: String = "",
    val memberId: String = "",
    val principal: Double = 0.0,
    val interestRate: Double = 10.0,
    val durationMonths: Int = 3,
    val requestedOn: String = "",
    val approvedOn: String = "",
    val dueDate: String = "",
    val status: LoanStatus = LoanStatus.PENDING,
    val penaltyRate: Double = 2.0,
    val approvedBy: String = "",
    val notes: String = ""
) {
    val totalRepayable: Double
        get() = principal + (principal * interestRate / 100.0)
}

@Serializable
data class Meeting(
    val id: String = "",
    val chamaId: String = "",
    val title: String = "",
    val agenda: String = "",
    val scheduledAt: String = "",
    val venue: String = "",
    val minutes: String = "",
    val attendeesPresent: Int = 0,
    val expectedAttendees: Int = 0
)

@Serializable
data class NotificationItem(
    val id: String = "",
    val chamaId: String = "",
    val title: String = "",
    val body: String = "",
    val type: String = "",
    val targetMemberId: String? = null,
    val createdAt: String = "",
    val read: Boolean = false
)

@Serializable
data class DashboardSummary(
    val chamaName: String = "",
    val totalSavings: Double = 0.0,
    val activeLoans: Double = 0.0,
    val outstandingBalance: Double = 0.0,
    val activeMembers: Int = 0,
    val meetingCount: Int = 0,
    val repaymentRate: Double = 0.0
)
