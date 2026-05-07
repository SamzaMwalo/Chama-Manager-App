//package com.chamamanager104.core.data
//
//import com.chamamanager104.core.local.ContributionDao
//import com.chamamanager104.core.local.LoanDao
//import com.chamamanager104.core.local.MeetingDao
//import com.chamamanager104.core.local.MemberDao
//import com.chamamanager104.core.local.MemberEntity
//import com.chamamanager104.core.local.NotificationDao
//import com.chamamanager104.core.local.asEntity
//import com.chamamanager104.core.local.asExternal
//import com.chamamanager104.core.model.Contribution
//import com.chamamanager104.core.model.ContributionFrequency
//import com.chamamanager104.core.model.DashboardSummary
//import com.chamamanager104.core.model.Loan
//import com.chamamanager104.core.model.LoanStatus
//import com.chamamanager104.core.model.Meeting
//import com.chamamanager104.core.model.Member
//import com.chamamanager104.core.model.NotificationItem
//import com.chamamanager104.core.model.UserRole
//import com.chamamanager104.core.model.UserSession
//import com.chamamanager104.core.network.MpesaApi
//import com.chamamanager104.core.network.MpesaStkPushRequest
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.ktx.Firebase
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//import java.util.UUID
//import javax.inject.Inject
//import javax.inject.Singleton
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.flatMapLatest
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//@Singleton
//class FirebaseAuthRepository @Inject constructor(
//    private val firestore: FirebaseFirestore,
//    private val sessionStore: SessionStore
//) : AuthRepository {
//
//    private val auth: FirebaseAuth = Firebase.auth
//
//    override val currentUser: Flow<UserSession?> = sessionStore.currentUser
//
//    init {
//        auth.currentUser?.uid?.let { uid ->
//            CoroutineScope(Dispatchers.IO).launch {
//                runCatching {
//                    val snapshot = firestore.collection("users").document(uid).get().await()
//                    sessionStore.update(snapshot.toUserSession(uid))
//                }
//            }
//        }
//    }
//
//    override suspend fun signIn(email: String, password: String): Result<UserSession> = runCatching {
//        val authResult = auth.signInWithEmailAndPassword(email, password).await()
//        val uid = authResult.user?.uid.orEmpty()
//        val snapshot = firestore.collection("users").document(uid).get().await()
//        snapshot.toUserSession(uid).also(sessionStore::update)
//    }
//
//    override suspend fun signUp(
//        fullName: String,
//        phoneNumber: String,
//        email: String,
//        password: String,
//        role: UserRole,
//        chamaName: String,
//        inviteCode: String
//    ): Result<UserSession> = runCatching {
//        val authResult = auth.createUserWithEmailAndPassword(email, password).await() ///
//        val uid = authResult.user?.uid.orEmpty() ///
//
//        val chama = if (inviteCode.isNotBlank()) {
//            val chamaSnapshot = firestore.collection("chamas")
//                .whereEqualTo("inviteCode", inviteCode.trim().uppercase(Locale.US))
//                .limit(1)
//                .get()
//                .await()
//
//            val doc = chamaSnapshot.documents.firstOrNull()
//                ?: error("No chama found for invite code ${inviteCode.trim()}")
//            Triple(doc.id, doc.getString("name").orEmpty(), doc.getString("inviteCode").orEmpty())
//        } else {
//            require(chamaName.isNotBlank()) { "Enter a chama name or an invite code" }
//            val chamaId = UUID.randomUUID().toString()
//            val code = generateInviteCode(chamaName)
//            firestore.collection("chamas").document(chamaId).set(
//                mapOf(
//                    "name" to chamaName.trim(),
//                    "inviteCode" to code,
//                    "description" to "Created from Android app",
//                    "createdAt" to System.currentTimeMillis()
//                )
//            ).await()
//            Triple(chamaId, chamaName.trim(), code)
//        }
//
////        val authResult = auth.createUserWithEmailAndPassword(email, password).await() ///removed now
////        val uid = authResult.user?.uid.orEmpty() ///removed now
//        val session = UserSession(
//            uid = uid,
//            fullName = fullName,
//            phoneNumber = phoneNumber,
//            email = email,
//            role = role,
//            chamaId = chama.first,
//            chamaName = chama.second,
//            chamaCode = chama.third
//        )
//
//        firestore.collection("users").document(uid).set(
//            mapOf(
//                "fullName" to fullName,
//                "phoneNumber" to phoneNumber,
//                "email" to email,
//                "role" to role.name,
//                "chamaId" to session.chamaId,
//                "chamaName" to session.chamaName,
//                "chamaCode" to session.chamaCode,
//                "createdAt" to System.currentTimeMillis()
//            )
//        ).await()
//
//        firestore.collection("members").document(uid).set(
//            mapOf(
//                "id" to uid,
//                "chamaId" to session.chamaId,
//                "fullName" to fullName,
//                "phoneNumber" to phoneNumber,
//                "email" to email,
//                "nationalId" to "",
//                "joinDate" to "2026-05-01",
//                "status" to "ACTIVE",
//                "role" to role.name,
//                "totalContribution" to 0.0,
//                "outstandingLoan" to 0.0,
//                "contributionFrequency" to "MONTHLY"
//            )
//        ).await()
//
//        sessionStore.update(session)
//        session
//    }
//
//    override suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
//        auth.sendPasswordResetEmail(email).await()
//    }
//
//    override suspend fun signOut() {
//        auth.signOut()
//        sessionStore.update(null)
//    }
//}
//
//@Singleton
//class OfflineFirstMemberRepository @Inject constructor(
//    private val memberDao: MemberDao,
//    private val firestore: FirebaseFirestore,
//    private val sessionStore: SessionStore
//) : MemberRepository {
//
//    override fun observeMembers(): Flow<List<Member>> {
//        return sessionStore.currentUser.flatMapLatest { session ->
//            if (session == null || session.chamaId.isBlank()) {
//                flowOf(emptyList())
//            } else {
////                memberDao.observeByChama(session.chamaId).map { items -> items.map { it.asExternal() } } /// removed
//                memberDao.observeByChama(session.chamaId)
//                    .onStart { syncMembersFromRemote(session.chamaId) }
//                    .map { items -> items.map { it.asExternal() } }
//            }
//        }
//    }
//
//    override suspend fun saveMember(member: Member): Result<Unit> = runCatching {
//        val session = requireCurrentSession()
//        val payload = if (member.id.isBlank()) {
//            member.copy(id = UUID.randomUUID().toString(), chamaId = session.chamaId)
//        } else {
//            member.copy(chamaId = session.chamaId)
//        }
//
//        memberDao.upsert(payload.asEntity())
//        firestore.collection("members").document(payload.id).set(
//            mapOf(
//                "id" to payload.id,
//                "chamaId" to payload.chamaId,
//                "fullName" to payload.fullName,
//                "phoneNumber" to payload.phoneNumber,
//                "email" to payload.email,
//                "nationalId" to payload.nationalId,
//                "joinDate" to payload.joinDate,
//                "status" to payload.status,
//                "role" to payload.role.name,
//                "totalContribution" to payload.totalContribution,
//                "outstandingLoan" to payload.outstandingLoan,
//                "contributionFrequency" to payload.contributionFrequency.name
//            )
//        ).await()
//    }
//
//    override suspend fun deleteMember(memberId: String): Result<Unit> = runCatching {
//        memberDao.deleteById(memberId)
//        firestore.collection("members").document(memberId).delete().await()
//    }
//
//    ///added
//    private suspend fun syncMembersFromRemote(chamaId: String) {
//        val snapshot = firestore.collection("members")
//            .whereEqualTo("chamaId", chamaId)
//            .get()
//            .await()
//
//        val remoteMembers = snapshot.documents.map { it.toMemberEntity(chamaId) }
//        memberDao.deleteByChama(chamaId)
//        memberDao.upsertAll(remoteMembers)
//    }
//    ///added
//
//    private suspend fun requireCurrentSession(): UserSession =
//        sessionStore.currentUser.first() ?: error("No signed-in user session")
//}
//
//@Singleton
//class OfflineFirstContributionRepository @Inject constructor(
//    private val contributionDao: ContributionDao,
//    private val firestore: FirebaseFirestore,
//    private val mpesaApi: MpesaApi,
//    private val sessionStore: SessionStore
//) : ContributionRepository {
//
//    override fun observeContributions(): Flow<List<Contribution>> {
//        return sessionStore.currentUser.flatMapLatest { session ->
//            if (session == null || session.chamaId.isBlank()) {
//                flowOf(emptyList())
//            } else {
//                contributionDao.observeByChama(session.chamaId).map { items -> items.map { it.asExternal() } }
//            }
//        }
//    }
//
//    override fun observeMemberContributions(memberId: String): Flow<List<Contribution>> {
//        return contributionDao.observeByMember(memberId).map { items -> items.map { it.asExternal() } }
//    }
//
//    override suspend fun recordContribution(contribution: Contribution): Result<Unit> = runCatching {
//        val session = requireCurrentSession()
//        val payload = if (contribution.id.isBlank()) {
//            contribution.copy(id = UUID.randomUUID().toString(), chamaId = session.chamaId)
//        } else {
//            contribution.copy(chamaId = session.chamaId)
//        }
//
//        contributionDao.upsert(payload.asEntity())
//        firestore.collection("contributions").document(payload.id).set(
//            mapOf(
//                "id" to payload.id,
//                "chamaId" to payload.chamaId,
//                "memberId" to payload.memberId,
//                "amount" to payload.amount,
//                "frequency" to payload.frequency.name,
//                "paidOn" to payload.paidOn,
//                "dueOn" to payload.dueOn,
//                "method" to payload.method,
//                "receiptNumber" to payload.receiptNumber,
//                "confirmed" to payload.confirmed
//            )
//        ).await()
//    }
//
//    override suspend fun initiateMpesaPayment(member: Member, amount: Double): Result<String> = runCatching {
//        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date())
//        val request = MpesaStkPushRequest(
//            businessShortCode = "174379",
//            password = "REPLACE_WITH_BASE64_SHORTCODE_PASSKEY_TIMESTAMP",
//            timestamp = timestamp,
//            transactionType = "CustomerPayBillOnline",
//            amount = amount.toInt().toString(),
//            partyA = member.phoneNumber,
//            partyB = "174379",
//            phoneNumber = member.phoneNumber,
//            callBackURL = "https://your-cloud-function-url/mpesaCallback",
//            accountReference = "Chama Manager 104",
//            transactionDesc = "Contribution payment"
//        )
//        val response = mpesaApi.triggerStkPush(
//            token = "Bearer REPLACE_WITH_OAUTH_TOKEN",
//            request = request
//        )
//        response.customerMessage ?: "STK Push sent"
//    }
//
//    private suspend fun requireCurrentSession(): UserSession =
//        sessionStore.currentUser.first() ?: error("No signed-in user session")
//}
//
//@Singleton
//class OfflineFirstLoanRepository @Inject constructor(
//    private val loanDao: LoanDao,
//    private val firestore: FirebaseFirestore,
//    private val sessionStore: SessionStore
//) : LoanRepository {
//
//    override fun observeLoans(): Flow<List<Loan>> {
//        return sessionStore.currentUser.flatMapLatest { session ->
//            if (session == null || session.chamaId.isBlank()) {
//                flowOf(emptyList())
//            } else {
//                loanDao.observeByChama(session.chamaId).map { items -> items.map { it.asExternal() } }
//            }
//        }
//    }
//
//    override suspend fun saveLoan(loan: Loan): Result<Unit> = runCatching {
//        val session = requireCurrentSession()
//        val payload = if (loan.id.isBlank()) {
//            loan.copy(id = UUID.randomUUID().toString(), chamaId = session.chamaId)
//        } else {
//            loan.copy(chamaId = session.chamaId)
//        }
//
//        loanDao.upsert(payload.asEntity())
//        firestore.collection("loans").document(payload.id).set(
//            mapOf(
//                "id" to payload.id,
//                "chamaId" to payload.chamaId,
//                "memberId" to payload.memberId,
//                "principal" to payload.principal,
//                "interestRate" to payload.interestRate,
//                "durationMonths" to payload.durationMonths,
//                "requestedOn" to payload.requestedOn,
//                "approvedOn" to payload.approvedOn,
//                "dueDate" to payload.dueDate,
//                "status" to payload.status.name,
//                "penaltyRate" to payload.penaltyRate,
//                "approvedBy" to payload.approvedBy,
//                "notes" to payload.notes
//            )
//        ).await()
//    }
//
//    private suspend fun requireCurrentSession(): UserSession =
//        sessionStore.currentUser.first() ?: error("No signed-in user session")
//}
//
//@Singleton
//class OfflineFirstMeetingRepository @Inject constructor(
//    private val meetingDao: MeetingDao,
//    private val firestore: FirebaseFirestore,
//    private val sessionStore: SessionStore
//) : MeetingRepository {
//
//    override fun observeMeetings(): Flow<List<Meeting>> {
//        return sessionStore.currentUser.flatMapLatest { session ->
//            if (session == null || session.chamaId.isBlank()) {
//                flowOf(emptyList())
//            } else {
//                meetingDao.observeByChama(session.chamaId).map { items -> items.map { it.asExternal() } }
//            }
//        }
//    }
//
//    override suspend fun saveMeeting(meeting: Meeting): Result<Unit> = runCatching {
//        val session = requireCurrentSession()
//        val payload = if (meeting.id.isBlank()) {
//            meeting.copy(id = UUID.randomUUID().toString(), chamaId = session.chamaId)
//        } else {
//            meeting.copy(chamaId = session.chamaId)
//        }
//
//        meetingDao.upsert(payload.asEntity())
//        firestore.collection("meetings").document(payload.id).set(
//            mapOf(
//                "id" to payload.id,
//                "chamaId" to payload.chamaId,
//                "title" to payload.title,
//                "agenda" to payload.agenda,
//                "scheduledAt" to payload.scheduledAt,
//                "venue" to payload.venue,
//                "minutes" to payload.minutes,
//                "attendeesPresent" to payload.attendeesPresent,
//                "expectedAttendees" to payload.expectedAttendees
//            )
//        ).await()
//    }
//
//    private suspend fun requireCurrentSession(): UserSession =
//        sessionStore.currentUser.first() ?: error("No signed-in user session")
//}
//
//@Singleton
//class OfflineFirstNotificationRepository @Inject constructor(
//    private val notificationDao: NotificationDao,
//    private val sessionStore: SessionStore
//) : NotificationRepository {
//
//    override fun observeNotifications(): Flow<List<NotificationItem>> {
//        return sessionStore.currentUser.flatMapLatest { session ->
//            if (session == null || session.chamaId.isBlank()) {
//                flowOf(emptyList())
//            } else {
//                notificationDao.observeByChama(session.chamaId).map { items -> items.map { it.asExternal() } }
//            }
//        }
//    }
//}
//
//@Singleton
//class DefaultDashboardRepository @Inject constructor(
//    private val authRepository: AuthRepository,
//    private val memberRepository: MemberRepository,
//    private val contributionRepository: ContributionRepository,
//    private val loanRepository: LoanRepository,
//    private val meetingRepository: MeetingRepository
//) : DashboardRepository {
//
//    override fun observeSummary(): Flow<DashboardSummary> {
//        return combine(
//            authRepository.currentUser,
//            memberRepository.observeMembers(),
//            contributionRepository.observeContributions(),
//            loanRepository.observeLoans(),
//            meetingRepository.observeMeetings()
//        ) { session: UserSession?,
//            members: List<Member>,
//            contributions: List<Contribution>,
//            loans: List<Loan>,
//            meetings: List<Meeting> ->
//
//            val totalSavings = contributions.sumOf { it.amount }
//            val activeLoans = loans
//                .filter { it.status == LoanStatus.ACTIVE || it.status == LoanStatus.OVERDUE }
//                .sumOf { it.totalRepayable }
//            val outstanding = activeLoans - contributions.sumOf { it.amount * 0.15 }
//
//            DashboardSummary(
//                chamaName = session?.chamaName.orEmpty(),
//                totalSavings = totalSavings,
//                activeLoans = activeLoans,
//                outstandingBalance = outstanding.coerceAtLeast(0.0),
//                activeMembers = members.count { it.status == "ACTIVE" },
//                meetingCount = meetings.size,
//                repaymentRate = if (loans.isEmpty()) 100.0 else (loans.count { it.status == LoanStatus.COMPLETED }.toDouble() / loans.size) * 100.0
//            )
//        }
//    }
//}
//
//private fun DocumentSnapshot.toUserSession(uid: String): UserSession {
//    val roleName = getString("role") ?: UserRole.MEMBER.name
//    return UserSession(
//        uid = uid,
//        fullName = getString("fullName").orEmpty(),
//        phoneNumber = getString("phoneNumber").orEmpty(),
//        email = getString("email").orEmpty(),
//        role = runCatching { UserRole.valueOf(roleName) }.getOrDefault(UserRole.MEMBER),
//        chamaId = getString("chamaId").orEmpty(),
//        chamaName = getString("chamaName").orEmpty(),
//        chamaCode = getString("chamaCode").orEmpty()
//    )
//}
///// added
//private fun DocumentSnapshot.toMemberEntity(chamaId: String): MemberEntity {
//    val roleName = getString("role") ?: UserRole.MEMBER.name
//    val frequencyName = getString("contributionFrequency") ?: ContributionFrequency.MONTHLY.name
//    return MemberEntity(
//        id = getString("id").orEmpty().ifBlank { id },
//        chamaId = getString("chamaId").orEmpty().ifBlank { chamaId },
//        fullName = getString("fullName").orEmpty(),
//        phoneNumber = getString("phoneNumber").orEmpty(),
//        email = getString("email").orEmpty(),
//        nationalId = getString("nationalId").orEmpty(),
//        joinDate = getString("joinDate").orEmpty(),
//        status = getString("status").orEmpty().ifBlank { "ACTIVE" },
//        role = runCatching { UserRole.valueOf(roleName) }.getOrDefault(UserRole.MEMBER),
//        totalContribution = getDouble("totalContribution") ?: 0.0,
//        outstandingLoan = getDouble("outstandingLoan") ?: 0.0,
//        contributionFrequency = runCatching { ContributionFrequency.valueOf(frequencyName) }
//            .getOrDefault(ContributionFrequency.MONTHLY)
//    )
//}
///// added
//
//private fun generateInviteCode(chamaName: String): String {
//    val prefix = chamaName.filter { it.isLetterOrDigit() }.take(4).uppercase(Locale.US).ifBlank { "CHMA" }
//    val suffix = UUID.randomUUID().toString().take(4).uppercase(Locale.US)
//    return "$prefix-$suffix"
//}

package com.chamamanager104.core.data

import com.chamamanager104.core.local.ContributionDao
import com.chamamanager104.core.local.ContributionEntity
import com.chamamanager104.core.local.LoanDao
import com.chamamanager104.core.local.MeetingDao
import com.chamamanager104.core.local.MeetingEntity
import com.chamamanager104.core.local.MemberDao
import com.chamamanager104.core.local.MemberEntity
import com.chamamanager104.core.local.NotificationDao
import com.chamamanager104.core.local.asEntity
import com.chamamanager104.core.local.asExternal
import com.chamamanager104.core.model.Contribution
import com.chamamanager104.core.model.ContributionFrequency
import com.chamamanager104.core.model.DashboardSummary
import com.chamamanager104.core.model.Loan
import com.chamamanager104.core.model.LoanStatus
import com.chamamanager104.core.model.Meeting
import com.chamamanager104.core.model.Member
import com.chamamanager104.core.model.NotificationItem
import com.chamamanager104.core.model.UserRole
import com.chamamanager104.core.model.UserSession
import com.chamamanager104.core.network.MpesaApi
import com.chamamanager104.core.network.MpesaStkPushRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionStore: SessionStore
) : AuthRepository {

    private val auth: FirebaseAuth = Firebase.auth

    override val currentUser: Flow<UserSession?> = sessionStore.currentUser

    init {
        auth.currentUser?.uid?.let { uid ->
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    val snapshot = firestore.collection("users").document(uid).get().await()
                    sessionStore.update(snapshot.toUserSession(uid))
                }
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<UserSession> = runCatching {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid.orEmpty()
        val snapshot = firestore.collection("users").document(uid).get().await()
        snapshot.toUserSession(uid).also(sessionStore::update)
    }

    override suspend fun signUp(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String,
        role: UserRole,
        chamaName: String,
        inviteCode: String
    ): Result<UserSession> = runCatching {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid.orEmpty()

        val chama = if (inviteCode.isNotBlank()) {
            val chamaSnapshot = firestore.collection("chamas")
                .whereEqualTo("inviteCode", inviteCode.trim().uppercase(Locale.US))
                .limit(1)
                .get()
                .await()

            val doc = chamaSnapshot.documents.firstOrNull()
                ?: error("No chama found for invite code ${inviteCode.trim()}")
            Triple(doc.id, doc.getString("name").orEmpty(), doc.getString("inviteCode").orEmpty())
        } else {
            require(chamaName.isNotBlank()) { "Enter a chama name or an invite code" }
            val chamaId = UUID.randomUUID().toString()
            val code = generateInviteCode(chamaName)
            firestore.collection("chamas").document(chamaId).set(
                mapOf(
                    "name" to chamaName.trim(),
                    "inviteCode" to code,
                    "description" to "Created from Android app",
                    "createdAt" to System.currentTimeMillis()
                )
            ).await()
            Triple(chamaId, chamaName.trim(), code)
        }

        val session = UserSession(
            uid = uid,
            fullName = fullName,
            phoneNumber = phoneNumber,
            email = email,
            role = role,
            chamaId = chama.first,
            chamaName = chama.second,
            chamaCode = chama.third
        )

        firestore.collection("users").document(uid).set(
            mapOf(
                "fullName" to fullName,
                "phoneNumber" to phoneNumber,
                "email" to email,
                "role" to role.name,
                "chamaId" to session.chamaId,
                "chamaName" to session.chamaName,
                "chamaCode" to session.chamaCode,
                "createdAt" to System.currentTimeMillis()
            )
        ).await()

        firestore.collection("members").document(uid).set(
            mapOf(
                "id" to uid,
                "chamaId" to session.chamaId,
                "fullName" to fullName,
                "phoneNumber" to phoneNumber,
                "email" to email,
                "nationalId" to "",
                "joinDate" to "2026-05-01",
                "status" to "ACTIVE",
                "role" to role.name,
                "totalContribution" to 0.0,
                "outstandingLoan" to 0.0,
                "contributionFrequency" to "MONTHLY"
            )
        ).await()

        sessionStore.update(session)
        session
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun signOut() {
        auth.signOut()
        sessionStore.update(null)
    }
}

@Singleton
class OfflineFirstMemberRepository @Inject constructor(
    private val memberDao: MemberDao,
    private val firestore: FirebaseFirestore,
    private val sessionStore: SessionStore
) : MemberRepository {

    override fun observeMembers(): Flow<List<Member>> {
        return sessionStore.currentUser.flatMapLatest { session ->
            if (session == null || session.chamaId.isBlank()) {
                flowOf(emptyList())
            } else {
                memberDao.observeByChama(session.chamaId)
                    .onStart { syncMembersFromRemote(session.chamaId) }
                    .map { items -> items.map { it.asExternal() } }
            }
        }
    }

    override suspend fun saveMember(member: Member): Result<Unit> = runCatching {
        val session = requireCurrentSession()
        val payload = if (member.id.isBlank()) {
            member.copy(id = UUID.randomUUID().toString(), chamaId = session.chamaId)
        } else {
            member.copy(chamaId = session.chamaId)
        }

        memberDao.upsert(payload.asEntity())
        firestore.collection("members").document(payload.id).set(
            mapOf(
                "id" to payload.id,
                "chamaId" to payload.chamaId,
                "fullName" to payload.fullName,
                "phoneNumber" to payload.phoneNumber,
                "email" to payload.email,
                "nationalId" to payload.nationalId,
                "joinDate" to payload.joinDate,
                "status" to payload.status,
                "role" to payload.role.name,
                "totalContribution" to payload.totalContribution,
                "outstandingLoan" to payload.outstandingLoan,
                "contributionFrequency" to payload.contributionFrequency.name
            )
        ).await()
    }

    override suspend fun deleteMember(memberId: String): Result<Unit> = runCatching {
        memberDao.deleteById(memberId)
        firestore.collection("members").document(memberId).delete().await()
    }

    private suspend fun syncMembersFromRemote(chamaId: String) {
        val snapshot = firestore.collection("members")
            .whereEqualTo("chamaId", chamaId)
            .get()
            .await()

        val remoteMembers = snapshot.documents.map { it.toMemberEntity(chamaId) }
        memberDao.deleteByChama(chamaId)
        memberDao.upsertAll(remoteMembers)
    }

    private suspend fun requireCurrentSession(): UserSession =
        sessionStore.currentUser.first() ?: error("No signed-in user session")
}

@Singleton
class OfflineFirstContributionRepository @Inject constructor(
    private val contributionDao: ContributionDao,
    private val memberDao: MemberDao,
    private val firestore: FirebaseFirestore,
    private val mpesaApi: MpesaApi,
    private val sessionStore: SessionStore
) : ContributionRepository {

    override fun observeContributions(): Flow<List<Contribution>> {
        return sessionStore.currentUser.flatMapLatest { session ->
            if (session == null || session.chamaId.isBlank()) {
                flowOf(emptyList())
            } else {
                contributionDao.observeByChama(session.chamaId)
                    .onStart { syncContributionsFromRemote(session.chamaId) }
                    .map { items -> items.map { it.asExternal() } }
            }
        }
    }

    override fun observeMemberContributions(memberId: String): Flow<List<Contribution>> {
        return contributionDao.observeByMember(memberId).map { items -> items.map { it.asExternal() } }
    }

    override suspend fun recordContribution(contribution: Contribution): Result<Unit> = runCatching {
        val session = requireCurrentSession()
        val payload = if (contribution.id.isBlank()) {
            contribution.copy(id = UUID.randomUUID().toString(), chamaId = session.chamaId)
        } else {
            contribution.copy(chamaId = session.chamaId)
        }

        val memberSnapshot = firestore.collection("members").document(payload.memberId).get().await()
        require(memberSnapshot.exists() && memberSnapshot.getString("chamaId") == session.chamaId) {
            "No member found in this chama for the supplied member ID"
        }

        contributionDao.upsert(payload.asEntity())
        firestore.collection("contributions").document(payload.id).set(
            mapOf(
                "id" to payload.id,
                "chamaId" to payload.chamaId,
                "memberId" to payload.memberId,
                "amount" to payload.amount,
                "frequency" to payload.frequency.name,
                "paidOn" to payload.paidOn,
                "dueOn" to payload.dueOn,
                "method" to payload.method,
                "receiptNumber" to payload.receiptNumber,
                "confirmed" to payload.confirmed
            )
        ).await()

        val totalContribution = contributionDao.totalByMember(session.chamaId, payload.memberId)
        memberDao.updateTotalContribution(payload.memberId, totalContribution)
        firestore.collection("members").document(payload.memberId)
            .update("totalContribution", totalContribution)
            .await()
    }

    override suspend fun initiateMpesaPayment(member: Member, amount: Double): Result<String> = runCatching {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date())
        val request = MpesaStkPushRequest(
            businessShortCode = "174379",
            password = "REPLACE_WITH_BASE64_SHORTCODE_PASSKEY_TIMESTAMP",
            timestamp = timestamp,
            transactionType = "CustomerPayBillOnline",
            amount = amount.toInt().toString(),
            partyA = member.phoneNumber,
            partyB = "174379",
            phoneNumber = member.phoneNumber,
            callBackURL = "https://your-cloud-function-url/mpesaCallback",
            accountReference = "Chama Manager 104",
            transactionDesc = "Contribution payment"
        )
        val response = mpesaApi.triggerStkPush(
            token = "Bearer REPLACE_WITH_OAUTH_TOKEN",
            request = request
        )
        response.customerMessage ?: "STK Push sent"
    }

    private suspend fun requireCurrentSession(): UserSession =
        sessionStore.currentUser.first() ?: error("No signed-in user session")

    private suspend fun syncContributionsFromRemote(chamaId: String) {
        val snapshot = firestore.collection("contributions")
            .whereEqualTo("chamaId", chamaId)
            .get()
            .await()

        val remoteContributions = snapshot.documents.map { it.toContributionEntity(chamaId) }
        contributionDao.deleteByChama(chamaId)
        contributionDao.upsertAll(remoteContributions)
    }
}

@Singleton
class OfflineFirstLoanRepository @Inject constructor(
    private val loanDao: LoanDao,
    private val firestore: FirebaseFirestore,
    private val sessionStore: SessionStore
) : LoanRepository {

    override fun observeLoans(): Flow<List<Loan>> {
        return sessionStore.currentUser.flatMapLatest { session ->
            if (session == null || session.chamaId.isBlank()) {
                flowOf(emptyList())
            } else {
                loanDao.observeByChama(session.chamaId).map { items -> items.map { it.asExternal() } }
            }
        }
    }

    override suspend fun saveLoan(loan: Loan): Result<Unit> = runCatching {
        val session = requireCurrentSession()
        val payload = if (loan.id.isBlank()) {
            loan.copy(id = UUID.randomUUID().toString(), chamaId = session.chamaId)
        } else {
            loan.copy(chamaId = session.chamaId)
        }

        loanDao.upsert(payload.asEntity())
        firestore.collection("loans").document(payload.id).set(
            mapOf(
                "id" to payload.id,
                "chamaId" to payload.chamaId,
                "memberId" to payload.memberId,
                "principal" to payload.principal,
                "interestRate" to payload.interestRate,
                "durationMonths" to payload.durationMonths,
                "requestedOn" to payload.requestedOn,
                "approvedOn" to payload.approvedOn,
                "dueDate" to payload.dueDate,
                "status" to payload.status.name,
                "penaltyRate" to payload.penaltyRate,
                "approvedBy" to payload.approvedBy,
                "notes" to payload.notes
            )
        ).await()
    }

    private suspend fun requireCurrentSession(): UserSession =
        sessionStore.currentUser.first() ?: error("No signed-in user session")
}

@Singleton
class OfflineFirstMeetingRepository @Inject constructor(
    private val meetingDao: MeetingDao,
    private val firestore: FirebaseFirestore,
    private val sessionStore: SessionStore
) : MeetingRepository {

    override fun observeMeetings(): Flow<List<Meeting>> {
        return sessionStore.currentUser.flatMapLatest { session ->
            if (session == null || session.chamaId.isBlank()) {
                flowOf(emptyList())
            } else {
                meetingDao.observeByChama(session.chamaId)
                    .onStart { syncMeetingsFromRemote(session.chamaId) }
                    .map { items -> items.map { it.asExternal() } }
            }
        }
    }

    override suspend fun saveMeeting(meeting: Meeting): Result<Unit> = runCatching {
        val session = requireCurrentSession()
        val payload = if (meeting.id.isBlank()) {
            meeting.copy(id = UUID.randomUUID().toString(), chamaId = session.chamaId)
        } else {
            meeting.copy(chamaId = session.chamaId)
        }

        meetingDao.upsert(payload.asEntity())
        firestore.collection("meetings").document(payload.id).set(
            mapOf(
                "id" to payload.id,
                "chamaId" to payload.chamaId,
                "title" to payload.title,
                "agenda" to payload.agenda,
                "scheduledAt" to payload.scheduledAt,
                "venue" to payload.venue,
                "minutes" to payload.minutes,
                "attendeesPresent" to payload.attendeesPresent,
                "expectedAttendees" to payload.expectedAttendees
            )
        ).await()
    }

    private suspend fun requireCurrentSession(): UserSession =
        sessionStore.currentUser.first() ?: error("No signed-in user session")

    private suspend fun syncMeetingsFromRemote(chamaId: String) {
        val snapshot = firestore.collection("meetings")
            .whereEqualTo("chamaId", chamaId)
            .get()
            .await()

        val remoteMeetings = snapshot.documents.map { it.toMeetingEntity(chamaId) }
        meetingDao.deleteByChama(chamaId)
        meetingDao.upsertAll(remoteMeetings)
    }
}

@Singleton
class OfflineFirstNotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao,
    private val sessionStore: SessionStore
) : NotificationRepository {

    override fun observeNotifications(): Flow<List<NotificationItem>> {
        return sessionStore.currentUser.flatMapLatest { session ->
            if (session == null || session.chamaId.isBlank()) {
                flowOf(emptyList())
            } else {
                notificationDao.observeByChama(session.chamaId).map { items -> items.map { it.asExternal() } }
            }
        }
    }
}

@Singleton
class DefaultDashboardRepository @Inject constructor(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val contributionRepository: ContributionRepository,
    private val loanRepository: LoanRepository,
    private val meetingRepository: MeetingRepository
) : DashboardRepository {

    override fun observeSummary(): Flow<DashboardSummary> {
        return combine(
            authRepository.currentUser,
            memberRepository.observeMembers(),
            contributionRepository.observeContributions(),
            loanRepository.observeLoans(),
            meetingRepository.observeMeetings()
        ) { session: UserSession?,
            members: List<Member>,
            contributions: List<Contribution>,
            loans: List<Loan>,
            meetings: List<Meeting> ->

            val totalSavings = contributions.sumOf { it.amount }
            val activeLoans = loans
                .filter { it.status == LoanStatus.ACTIVE || it.status == LoanStatus.OVERDUE }
                .sumOf { it.totalRepayable }
            val outstanding = activeLoans - contributions.sumOf { it.amount * 0.15 }

            DashboardSummary(
                chamaName = session?.chamaName.orEmpty(),
                totalSavings = totalSavings,
                activeLoans = activeLoans,
                outstandingBalance = outstanding.coerceAtLeast(0.0),
                activeMembers = members.count { it.status == "ACTIVE" },
                meetingCount = meetings.size,
                repaymentRate = if (loans.isEmpty()) 100.0 else (loans.count { it.status == LoanStatus.COMPLETED }.toDouble() / loans.size) * 100.0
            )
        }
    }
}

private fun DocumentSnapshot.toUserSession(uid: String): UserSession {
    val roleName = getString("role") ?: UserRole.MEMBER.name
    return UserSession(
        uid = uid,
        fullName = getString("fullName").orEmpty(),
        phoneNumber = getString("phoneNumber").orEmpty(),
        email = getString("email").orEmpty(),
        role = runCatching { UserRole.valueOf(roleName) }.getOrDefault(UserRole.MEMBER),
        chamaId = getString("chamaId").orEmpty(),
        chamaName = getString("chamaName").orEmpty(),
        chamaCode = getString("chamaCode").orEmpty()
    )
}

private fun DocumentSnapshot.toMemberEntity(chamaId: String): MemberEntity {
    val roleName = getString("role") ?: UserRole.MEMBER.name
    val frequencyName = getString("contributionFrequency") ?: ContributionFrequency.MONTHLY.name
    return MemberEntity(
        id = getString("id").orEmpty().ifBlank { id },
        chamaId = getString("chamaId").orEmpty().ifBlank { chamaId },
        fullName = getString("fullName").orEmpty(),
        phoneNumber = getString("phoneNumber").orEmpty(),
        email = getString("email").orEmpty(),
        nationalId = getString("nationalId").orEmpty(),
        joinDate = getString("joinDate").orEmpty(),
        status = getString("status").orEmpty().ifBlank { "ACTIVE" },
        role = runCatching { UserRole.valueOf(roleName) }.getOrDefault(UserRole.MEMBER),
        totalContribution = getDouble("totalContribution") ?: 0.0,
        outstandingLoan = getDouble("outstandingLoan") ?: 0.0,
        contributionFrequency = runCatching { ContributionFrequency.valueOf(frequencyName) }
            .getOrDefault(ContributionFrequency.MONTHLY)
    )
}

private fun DocumentSnapshot.toContributionEntity(chamaId: String): ContributionEntity {
    val frequencyName = getString("frequency") ?: ContributionFrequency.MONTHLY.name
    return ContributionEntity(
        id = getString("id").orEmpty().ifBlank { id },
        chamaId = getString("chamaId").orEmpty().ifBlank { chamaId },
        memberId = getString("memberId").orEmpty(),
        amount = getDouble("amount") ?: 0.0,
        frequency = runCatching { ContributionFrequency.valueOf(frequencyName) }
            .getOrDefault(ContributionFrequency.MONTHLY),
        paidOn = getString("paidOn").orEmpty(),
        dueOn = getString("dueOn").orEmpty(),
        method = getString("method").orEmpty().ifBlank { "M-PESA" },
        receiptNumber = getString("receiptNumber").orEmpty(),
        confirmed = getBoolean("confirmed") ?: false
    )
}

private fun DocumentSnapshot.toMeetingEntity(chamaId: String): MeetingEntity {
    return MeetingEntity(
        id = getString("id").orEmpty().ifBlank { id },
        chamaId = getString("chamaId").orEmpty().ifBlank { chamaId },
        title = getString("title").orEmpty(),
        agenda = getString("agenda").orEmpty(),
        scheduledAt = getString("scheduledAt").orEmpty(),
        venue = getString("venue").orEmpty(),
        minutes = getString("minutes").orEmpty(),
        attendeesPresent = getLong("attendeesPresent")?.toInt() ?: 0,
        expectedAttendees = getLong("expectedAttendees")?.toInt() ?: 0
    )
}

private fun generateInviteCode(chamaName: String): String {
    val prefix = chamaName.filter { it.isLetterOrDigit() }.take(4).uppercase(Locale.US).ifBlank { "CHMA" }
    val suffix = UUID.randomUUID().toString().take(4).uppercase(Locale.US)
    return "$prefix-$suffix"
}
