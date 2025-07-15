package com.example.haven.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haven.viewModel.PaymentViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    amount: String,
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    var phoneNumber by remember { mutableStateOf("") }
    val paymentState by viewModel.paymentState.collectAsState()

    // Debug logging
    LaunchedEffect(paymentState) {
        Log.d("PaymentScreen", "Payment state changed: $paymentState")
    }

    // Clear payment state when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetPaymentState()
        }
    }

    // Navigate after successful payment
    LaunchedEffect(paymentState.success) {
        if (paymentState.success == true) {
            Log.d("PaymentScreen", "Payment successful, navigating to home")
            // Wait 2 seconds then navigate to home
            kotlinx.coroutines.delay(2000)
            navController.navigate("home") {
                popUpTo("payment/{amount}") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("M-Pesa Payment") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Pay KSH $amount",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("M-Pesa Phone Number") },
                placeholder = { Text("07XXXXXXXX or 254XXXXXXX") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    autoCorrectEnabled = false
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("Enter phone number in 07XXXXXXXX or 254XXXXXXX format")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (paymentState.isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Processing payment...")
            }

            paymentState.message?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            paymentState.success == true -> MaterialTheme.colorScheme.primaryContainer
                            paymentState.success == false -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Text(
                        text = message,
                        color = when {
                            paymentState.success == true -> MaterialTheme.colorScheme.onPrimaryContainer
                            paymentState.success == false -> MaterialTheme.colorScheme.onErrorContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (phoneNumber.isValidMpesaNumber()) {
                        val formattedPhone = phoneNumber.toMpesaFormat()
                        Log.d("PaymentScreen", "Initiating payment: phone=$formattedPhone, amount=$amount")
                        viewModel.initiatePayment(
                            phone = formattedPhone,
                            amount = amount.filter { it.isDigit() }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !paymentState.isLoading && phoneNumber.isValidMpesaNumber()
            ) {
                Text(
                    text = if (paymentState.isLoading) "Processing..." else "Confirm Payment",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Add a "Try Again" button after failed payments
            if (paymentState.success == false) {
                OutlinedButton(
                    onClick = {
                        viewModel.resetPaymentState()
                        phoneNumber = "" // Clear phone number for retry
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Try Again")
                }
            }

            // Add a button to check payment status if transaction ID exists
            paymentState.transactionId?.let { transactionId ->
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { viewModel.checkPaymentStatus(transactionId) },
                    enabled = !paymentState.isLoading
                ) {
                    Text("Check Payment Status")
                }
            }
        }
    }
}

// Updated validation function - accepts both formats
private fun String.isValidMpesaNumber(): Boolean {
    return when {
        // 07XXXXXXXX format
        matches("^07[0-9]{8}$".toRegex()) -> true
        // 254XXXXXXX format
        matches("^254[0-9]{9}$".toRegex()) -> true
        else -> false
    }
}

// Convert phone number to the format your API expects
private fun String.toMpesaFormat(): String {
    return when {
        // If starts with 07, convert to 254
        startsWith("07") -> "254${substring(1)}"
        // If starts with 254, keep as is
        startsWith("254") -> this
        else -> this
    }
}