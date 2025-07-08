package com.example.haven.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haven.network.ApiService
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

    // StateFlow for payment state
    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    fun initiatePayment(phone: String, amount: String) {
        // Validate phone number format first
        if (!phone.isValidMpesaNumber()) {
            _paymentState.value = PaymentState(
                success = false,
                message = "Invalid M-Pesa number format"
            )
            return
        }

        // Set loading state
        _paymentState.value = PaymentState(isLoading = true)

        viewModelScope.launch {
            try {
                // Convert to 254 format (e.g., 0722123456 -> 254722123456)
                val formattedPhone = "254${phone.substring(1)}"

                // Make API call
                val response = apiService.initiateStkPush(
                    phone = formattedPhone,
                    amount = amount.filter { it.isDigit() } // Ensure only digits
                )

                // Handle response
                if (response.isSuccessful && response.body()?.success == true) {
                    _paymentState.value = PaymentState(
                        success = true,
                        message = "Payment request sent to your phone"
                    )
                } else {
                    _paymentState.value = PaymentState(
                        success = false,
                        message = response.body()?.error ?: "Payment failed"
                    )
                }
            } catch (e: Exception) {
                _paymentState.value = PaymentState(
                    success = false,
                    message = "Network error: ${e.localizedMessage}"
                )
            } finally {
                // Reset loading state
                _paymentState.value = _paymentState.value.copy(isLoading = false)
            }
        }
    }
}

// Extension function for phone validation
private fun String.isValidMpesaNumber(): Boolean {
    return matches("^07[0-9]{8}$".toRegex())
}

// Data class remains the same
data class PaymentState(
    val isLoading: Boolean = false,
    val success: Boolean? = null,
    val message: String? = null
)