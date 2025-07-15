package com.example.haven.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haven.network.ApiService
import com.example.haven.network.PaymentRequest
import com.example.haven.network.StatusRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    private var lastTransactionId: String? = null

    fun initiatePayment(phone: String, amount: String) {
        viewModelScope.launch {
            try {
                _paymentState.value = PaymentState(isLoading = true)
                val authToken = getAuthToken()

                val response = apiService.initiateStkPush(
                    authToken = authToken,
                    request = PaymentRequest(
                        phone = phone,
                        amount = amount
                    )
                )

                // Log response details AFTER the API call
                Log.d("PaymentViewModel", "Response code: ${response.code()}")
                Log.d("PaymentViewModel", "Response body: ${response.body()}")
                Log.d("PaymentViewModel", "Response success: ${response.isSuccessful}")

                when {
                    response.isSuccessful -> {
                        val paymentResponse = response.body()
                        if (paymentResponse != null) {
                            if (paymentResponse.success) {
                                lastTransactionId = paymentResponse.checkoutRequestID
                                _paymentState.value = PaymentState(
                                    isLoading = false,
                                    success = true,
                                    message = paymentResponse.message ?: "Payment initiated successfully",
                                    transactionId = paymentResponse.checkoutRequestID
                                )
                            } else {
                                _paymentState.value = PaymentState(
                                    isLoading = false,
                                    success = false,
                                    message = paymentResponse.error ?: "Payment failed"
                                )
                            }
                        } else {
                            // Handle null response body
                            _paymentState.value = PaymentState(
                                isLoading = false,
                                success = false,
                                message = "Invalid response from server"
                            )
                        }
                    }
                    else -> {
                        // Handle HTTP error responses
                        _paymentState.value = PaymentState(
                            isLoading = false,
                            success = false,
                            message = "Server error: ${response.code()} ${response.message()}"
                        )
                    }
                }
            } catch (e: Exception) {
                _paymentState.value = PaymentState(
                    isLoading = false,
                    success = false,
                    message = "Network error: ${e.localizedMessage}"
                )
            }
        }
    }

    fun checkPaymentStatus(transactionId: String? = lastTransactionId) {
        transactionId ?: return

        viewModelScope.launch {
            try {
                _paymentState.value = _paymentState.value.copy(isLoading = true)

                val response = apiService.checkPaymentStatus(
                    authToken = getAuthToken(),
                    request = StatusRequest(transactionId)
                )

                when {
                    response.isSuccessful -> {
                        val statusResponse = response.body()
                        if (statusResponse != null) {
                            _paymentState.value = PaymentState(
                                isLoading = false,
                                success = statusResponse.status == "COMPLETED",
                                message = when (statusResponse.status) {
                                    "COMPLETED" -> "Payment confirmed successfully"
                                    "PENDING" -> "Payment pending"
                                    "FAILED" -> "Payment failed"
                                    else -> "Unknown payment status: ${statusResponse.status}"
                                },
                                transactionId = transactionId
                            )
                        } else {
                            _paymentState.value = PaymentState(
                                isLoading = false,
                                success = false,
                                message = "Invalid status response from server"
                            )
                        }
                    }
                    else -> {
                        _paymentState.value = PaymentState(
                            isLoading = false,
                            success = false,
                            message = "Status check failed: ${response.code()} ${response.message()}"
                        )
                    }
                }
            } catch (e: Exception) {
                _paymentState.value = PaymentState(
                    isLoading = false,
                    success = false,
                    message = "Status check failed: ${e.localizedMessage}"
                )
            }
        }
    }

    fun resetPaymentState() {
        _paymentState.value = PaymentState()
    }

    private fun getAuthToken(): String {
        return "Bearer your_api_key_here" // Replace with actual implementation
    }
}

data class PaymentState(
    val isLoading: Boolean = false,
    val success: Boolean? = null,
    val message: String? = null,
    val transactionId: String? = null
)