package com.example.haven.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("stkpush")
    suspend fun initiateStkPush(
        @Body request: PaymentRequest
    ): Response<PaymentResponse>
}

data class PaymentRequest(
    val phone: String,
    val amount: String
)

data class PaymentResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null
)