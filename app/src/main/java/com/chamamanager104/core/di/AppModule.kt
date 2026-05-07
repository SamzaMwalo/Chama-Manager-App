package com.chamamanager104.core.di

import android.content.Context
import androidx.room.Room
import com.chamamanager104.core.data.AuthRepository
import com.chamamanager104.core.data.ContributionRepository
import com.chamamanager104.core.data.DashboardRepository
import com.chamamanager104.core.data.DefaultDashboardRepository
import com.chamamanager104.core.data.FirebaseAuthRepository
import com.chamamanager104.core.data.LoanRepository
import com.chamamanager104.core.data.MeetingRepository
import com.chamamanager104.core.data.MemberRepository
import com.chamamanager104.core.data.NotificationRepository
import com.chamamanager104.core.data.OfflineFirstContributionRepository
import com.chamamanager104.core.data.OfflineFirstLoanRepository
import com.chamamanager104.core.data.OfflineFirstMeetingRepository
import com.chamamanager104.core.data.OfflineFirstMemberRepository
import com.chamamanager104.core.data.OfflineFirstNotificationRepository
import com.chamamanager104.core.local.ChamaDatabase
import com.chamamanager104.core.network.MpesaApi
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object InfrastructureModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ChamaDatabase =
        Room.databaseBuilder(context, ChamaDatabase::class.java, "chama_manager104.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMemberDao(database: ChamaDatabase) = database.memberDao()

    @Provides
    fun provideContributionDao(database: ChamaDatabase) = database.contributionDao()

    @Provides
    fun provideLoanDao(database: ChamaDatabase) = database.loanDao()

    @Provides
    fun provideMeetingDao(database: ChamaDatabase) = database.meetingDao()

    @Provides
    fun provideNotificationDao(database: ChamaDatabase) = database.notificationDao()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideMpesaApi(): MpesaApi {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder().addInterceptor(logger).build()

        return Retrofit.Builder()
            .baseUrl("https://sandbox.safaricom.co.ke/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MpesaApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(impl: FirebaseAuthRepository): AuthRepository

    @Binds
    abstract fun bindMemberRepository(impl: OfflineFirstMemberRepository): MemberRepository

    @Binds
    abstract fun bindContributionRepository(impl: OfflineFirstContributionRepository): ContributionRepository

    @Binds
    abstract fun bindLoanRepository(impl: OfflineFirstLoanRepository): LoanRepository

    @Binds
    abstract fun bindMeetingRepository(impl: OfflineFirstMeetingRepository): MeetingRepository

    @Binds
    abstract fun bindNotificationRepository(impl: OfflineFirstNotificationRepository): NotificationRepository

    @Binds
    abstract fun bindDashboardRepository(impl: DefaultDashboardRepository): DashboardRepository
}
