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
import com.example.haven.network.PaymentRequest

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    fun initiatePayment(phone: String, amount: String) {
        _paymentState.value = PaymentState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = apiService.initiateStkPush(
                    PaymentRequest(
                        phone = phone,
                        amount = amount
                    )
                )

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
                _paymentState.value = _paymentState.value.copy(isLoading = false)
            }
        }
    }
}

data class PaymentState(
    val isLoading: Boolean = false,
    val success: Boolean? = null,
    val message: String? = null
)