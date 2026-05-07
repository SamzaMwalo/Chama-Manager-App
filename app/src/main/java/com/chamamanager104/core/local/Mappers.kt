package com.chamamanager104.core.local

import com.chamamanager104.core.model.Contribution
import com.chamamanager104.core.model.Loan
import com.chamamanager104.core.model.Meeting
import com.chamamanager104.core.model.Member
import com.chamamanager104.core.model.NotificationItem

fun MemberEntity.asExternal() = Member(
    id = id,
    chamaId = chamaId,
    fullName = fullName,
    phoneNumber = phoneNumber,
    email = email,
    nationalId = nationalId,
    joinDate = joinDate,
    status = status,
    role = role,
    totalContribution = totalContribution,
    outstandingLoan = outstandingLoan,
    contributionFrequency = contributionFrequency
)

fun Member.asEntity() = MemberEntity(
    id = id,
    chamaId = chamaId,
    fullName = fullName,
    phoneNumber = phoneNumber,
    email = email,
    nationalId = nationalId,
    joinDate = joinDate,
    status = status,
    role = role,
    totalContribution = totalContribution,
    outstandingLoan = outstandingLoan,
    contributionFrequency = contributionFrequency
)

fun ContributionEntity.asExternal() = Contribution(
    id = id,
    chamaId = chamaId,
    memberId = memberId,
    amount = amount,
    frequency = frequency,
    paidOn = paidOn,
    dueOn = dueOn,
    method = method,
    receiptNumber = receiptNumber,
    confirmed = confirmed
)

fun Contribution.asEntity() = ContributionEntity(
    id = id,
    chamaId = chamaId,
    memberId = memberId,
    amount = amount,
    frequency = frequency,
    paidOn = paidOn,
    dueOn = dueOn,
    method = method,
    receiptNumber = receiptNumber,
    confirmed = confirmed
)

fun LoanEntity.asExternal() = Loan(
    id = id,
    chamaId = chamaId,
    memberId = memberId,
    principal = principal,
    interestRate = interestRate,
    durationMonths = durationMonths,
    requestedOn = requestedOn,
    approvedOn = approvedOn,
    dueDate = dueDate,
    status = status,
    penaltyRate = penaltyRate,
    approvedBy = approvedBy,
    notes = notes
)

fun Loan.asEntity() = LoanEntity(
    id = id,
    chamaId = chamaId,
    memberId = memberId,
    principal = principal,
    interestRate = interestRate,
    durationMonths = durationMonths,
    requestedOn = requestedOn,
    approvedOn = approvedOn,
    dueDate = dueDate,
    status = status,
    penaltyRate = penaltyRate,
    approvedBy = approvedBy,
    notes = notes
)

fun MeetingEntity.asExternal() = Meeting(
    id = id,
    chamaId = chamaId,
    title = title,
    agenda = agenda,
    scheduledAt = scheduledAt,
    venue = venue,
    minutes = minutes,
    attendeesPresent = attendeesPresent,
    expectedAttendees = expectedAttendees
)

fun Meeting.asEntity() = MeetingEntity(
    id = id,
    chamaId = chamaId,
    title = title,
    agenda = agenda,
    scheduledAt = scheduledAt,
    venue = venue,
    minutes = minutes,
    attendeesPresent = attendeesPresent,
    expectedAttendees = expectedAttendees
)

fun NotificationEntity.asExternal() = NotificationItem(
    id = id,
    chamaId = chamaId,
    title = title,
    body = body,
    type = type,
    targetMemberId = targetMemberId,
    createdAt = createdAt,
    read = read
)
