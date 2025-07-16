package com.example.haven.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("stk-push")
    suspend fun initiateStkPush(
        @Header("Authorization") authToken: String? = null,
        @Body request: PaymentRequest
    ): Response<PaymentResponse>


    @POST("callback/confirmation")
    suspend fun checkPaymentStatus(
        @Header("Authorization") authToken: String? = null,
        @Body request: StatusRequest
    ): Response<PaymentStatusResponse>
}


//data class PaymentRequest(
    //val phone: String,
    //val amount: String,
    //val reference: String? = null,
    //val callbackUri: String? = null,
    //val accountReference: String? = "Haven Spa"
//)


data class PaymentResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null,
    val checkoutRequestID: String? = null
)
data class StatusRequest(
    val transactionID: String
)

data class PaymentStatusResponse(
    val status: String,
    val amount: String? = null,
    val transactionDate: String? = null
)