//package com.chamamanager104.core.local
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface MemberDao {
//    @Query("SELECT * FROM members WHERE chamaId = :chamaId ORDER BY fullName ASC")
//    fun observeByChama(chamaId: String): Flow<List<MemberEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsert(item: MemberEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertAll(items: List<MemberEntity>)
//
//    ///added
//    @Query("DELETE FROM members WHERE chamaId = :chamaId")
//    suspend fun deleteByChama(chamaId: String)
//    ///added
//
//    @Query("DELETE FROM members WHERE id = :memberId")
//    suspend fun deleteById(memberId: String)
//}
//
//@Dao
//interface ContributionDao {
//    @Query("SELECT * FROM contributions WHERE chamaId = :chamaId ORDER BY paidOn DESC")
//    fun observeByChama(chamaId: String): Flow<List<ContributionEntity>>
//
//    @Query("SELECT * FROM contributions WHERE memberId = :memberId ORDER BY paidOn DESC")
//    fun observeByMember(memberId: String): Flow<List<ContributionEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsert(item: ContributionEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertAll(items: List<ContributionEntity>)
//}
//
//@Dao
//interface LoanDao {
//    @Query("SELECT * FROM loans WHERE chamaId = :chamaId ORDER BY requestedOn DESC")
//    fun observeByChama(chamaId: String): Flow<List<LoanEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsert(item: LoanEntity)
//}
//
//@Dao
//interface MeetingDao {
//    @Query("SELECT * FROM meetings WHERE chamaId = :chamaId ORDER BY scheduledAt DESC")
//    fun observeByChama(chamaId: String): Flow<List<MeetingEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsert(item: MeetingEntity)
//}
//
//@Dao
//interface NotificationDao {
//    @Query("SELECT * FROM notifications WHERE chamaId = :chamaId ORDER BY createdAt DESC")
//    fun observeByChama(chamaId: String): Flow<List<NotificationEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertAll(items: List<NotificationEntity>)
//}

package com.chamamanager104.core.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members WHERE chamaId = :chamaId ORDER BY fullName ASC")
    fun observeByChama(chamaId: String): Flow<List<MemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: MemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<MemberEntity>)

    @Query("UPDATE members SET totalContribution = :totalContribution WHERE id = :memberId")
    suspend fun updateTotalContribution(memberId: String, totalContribution: Double)

    @Query("DELETE FROM members WHERE chamaId = :chamaId")
    suspend fun deleteByChama(chamaId: String)

    @Query("DELETE FROM members WHERE id = :memberId")
    suspend fun deleteById(memberId: String)
}

@Dao
interface ContributionDao {
    @Query("SELECT * FROM contributions WHERE chamaId = :chamaId ORDER BY paidOn DESC")
    fun observeByChama(chamaId: String): Flow<List<ContributionEntity>>

    @Query("SELECT * FROM contributions WHERE memberId = :memberId ORDER BY paidOn DESC")
    fun observeByMember(memberId: String): Flow<List<ContributionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ContributionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ContributionEntity>)

    @Query("SELECT COALESCE(SUM(amount), 0) FROM contributions WHERE chamaId = :chamaId AND memberId = :memberId")
    suspend fun totalByMember(chamaId: String, memberId: String): Double

    @Query("DELETE FROM contributions WHERE chamaId = :chamaId")
    suspend fun deleteByChama(chamaId: String)
}

@Dao
interface LoanDao {
    @Query("SELECT * FROM loans WHERE chamaId = :chamaId ORDER BY requestedOn DESC")
    fun observeByChama(chamaId: String): Flow<List<LoanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: LoanEntity)
}

@Dao
interface MeetingDao {
    @Query("SELECT * FROM meetings WHERE chamaId = :chamaId ORDER BY scheduledAt DESC")
    fun observeByChama(chamaId: String): Flow<List<MeetingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: MeetingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<MeetingEntity>)

    @Query("DELETE FROM meetings WHERE chamaId = :chamaId")
    suspend fun deleteByChama(chamaId: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE chamaId = :chamaId ORDER BY createdAt DESC")
    fun observeByChama(chamaId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<NotificationEntity>)
}
