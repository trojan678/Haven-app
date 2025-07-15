package com.example.haven.network


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