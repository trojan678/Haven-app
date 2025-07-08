package com.example.haven.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/v1/mpesa/stkpush")
    suspend fun initiateStkPush(
        //@Header("Authorization") authToken: String? = null,
        @Body request: PaymentRequest
    ): Response<PaymentResponse>

    /**
     * Check payment status
     * @param request Payment status request containing transaction ID
     */
    @POST("api/v1/mpesa/status")
    suspend fun checkPaymentStatus(
        @Body request: StatusRequest
    ): Response<PaymentStatusResponse>
}

/**
 * Request model for STK Push
 * @property phone Phone number in 2547XXXXXXXX format
 * @property amount Payment amount (in KSH)
 * @property reference Optional payment reference
 */
//data class PaymentRequest(
    //val phone: String,
   // val amount: String,
    //val reference: String? = null  // Additional optional field
//)

/**
 * Response model for payment operations
 * @property success Indicates if operation was successful
 * @property message Human-readable message
 * @property error Error details if success=false
 * @property checkoutRequestID M-Pesa checkout request ID (for status checks)
 */
data class PaymentResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null,
    val checkoutRequestID: String? = null
)

/**
 * Request model for payment status check
 * @property transactionID M-Pesa transaction ID
 */
data class StatusRequest(
    val transactionID: String
)

/**
 * Payment status response
 * @property status Current payment status (PENDING, COMPLETED, FAILED)
 * @property amount Paid amount (if completed)
 */
data class PaymentStatusResponse(
    val status: String,
    val amount: String? = null,
    val transactionDate: String? = null
)