package com.chamamanager104.core.local

import androidx.room.TypeConverter
import com.chamamanager104.core.model.ContributionFrequency
import com.chamamanager104.core.model.LoanStatus
import com.chamamanager104.core.model.UserRole

class RoomConverters {
    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromContributionFrequency(value: ContributionFrequency): String = value.name

    @TypeConverter
    fun toContributionFrequency(value: String): ContributionFrequency = ContributionFrequency.valueOf(value)

    @TypeConverter
    fun fromLoanStatus(value: LoanStatus): String = value.name

    @TypeConverter
    fun toLoanStatus(value: String): LoanStatus = LoanStatus.valueOf(value)
}
