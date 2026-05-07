package com.chamamanager104.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        MemberEntity::class,
        ContributionEntity::class,
        LoanEntity::class,
        MeetingEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class ChamaDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun contributionDao(): ContributionDao
    abstract fun loanDao(): LoanDao
    abstract fun meetingDao(): MeetingDao
    abstract fun notificationDao(): NotificationDao
}
