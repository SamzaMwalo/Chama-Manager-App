# Database Schema

## Core collections

### `chamas/{chamaId}`

- `name`
- `inviteCode`
- `description`
- `createdAt`

### `users/{uid}`

- `fullName`
- `phoneNumber`
- `email`
- `role`
- `chamaId`
- `chamaName`
- `chamaCode`
- `createdAt`

### `members/{memberId}`

- `id`
- `chamaId`
- `fullName`
- `phoneNumber`
- `email`
- `nationalId`
- `joinDate`
- `status`
- `role`
- `totalContribution`
- `outstandingLoan`
- `contributionFrequency`

### `contributions/{contributionId}`

- `id`
- `chamaId`
- `memberId`
- `amount`
- `frequency`
- `paidOn`
- `dueOn`
- `method`
- `receiptNumber`
- `confirmed`

### `loans/{loanId}`

- `id`
- `chamaId`
- `memberId`
- `principal`
- `interestRate`
- `durationMonths`
- `requestedOn`
- `approvedOn`
- `dueDate`
- `status`
- `penaltyRate`
- `approvedBy`
- `notes`

### `meetings/{meetingId}`

- `id`
- `chamaId`
- `title`
- `agenda`
- `scheduledAt`
- `venue`
- `minutes`
- `attendeesPresent`
- `expectedAttendees`

### `notifications/{notificationId}`

- `id`
- `chamaId`
- `title`
- `body`
- `type`
- `targetMemberId`
- `createdAt`
- `read`

### `mpesaLogs/{logId}`

- `payload`
- `resultCode`
- `resultDesc`
- `createdAt`

## Local Room tables

- `members`
- `contributions`
- `loans`
- `meetings`
- `notifications`
