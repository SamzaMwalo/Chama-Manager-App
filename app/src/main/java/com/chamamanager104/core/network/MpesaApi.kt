package com.chamamanager104.core.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class MpesaStkPushRequest(
    val businessShortCode: String,
    val password: String,
    val timestamp: String,
    val transactionType: String,
    val amount: String,
    val partyA: String,
    val partyB: String,
    val phoneNumber: String,
    val callBackURL: String,
    val accountReference: String,
    val transactionDesc: String
)

data class MpesaStkPushResponse(
    val merchantRequestID: String? = null,
    val checkoutRequestID: String? = null,
    val responseCode: String? = null,
    val responseDescription: String? = null,
    val customerMessage: String? = null
)

interface MpesaApi {
    @POST("mpesa/stkpush/v1/processrequest")
    suspend fun triggerStkPush(
        @Header("Authorization") token: String,
        @Body request: MpesaStkPushRequest
    ): MpesaStkPushResponse
}
