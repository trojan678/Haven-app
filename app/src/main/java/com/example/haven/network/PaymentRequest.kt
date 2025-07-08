package com.example.haven.network

/**
 * M-Pesa payment request payload
 * @property phone Must start with 254 (e.g. 254712345678)
 * @property amount Numeric string (e.g. "100.50")
 */
data class PaymentRequest(
    val phone: String,
    val amount: String
) {
    init {
        require(phone.startsWith("254")) { "Phone must be in 254 format" }
        require(amount.matches(Regex("""^\d+(\.\d{1,2})?$"""))) {
            "Invalid amount format"
        }
    }
}