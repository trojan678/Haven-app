package com.example.haven.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haven.R
import com.example.haven.viewModel.PaymentViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.ui.draw.clip

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
            kotlinx.coroutines.delay(3000)
            navController.navigate("home") {
                popUpTo("payment/{amount}") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.texture5),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Semi-transparent overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("M-Pesa Payment", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Payment Amount Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E3A8A).copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Amount to Pay",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "KSH $amount",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Payment Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF334155).copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
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
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                                unfocusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                                cursorColor = Color.White
                            ),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "Phone format info",
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                            },
                            supportingText = {
                                Text(
                                    "Enter phone number in 07XXXXXXXX or 254XXXXXXX format",
                                    color = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        if (paymentState.isLoading) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Processing payment...",
                                    color = Color.White
                                )
                            }
                        }

                        paymentState.message?.let { message ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = when (paymentState.success) {
                                        true -> Color(0xFF065F46).copy(alpha = 0.9f)
                                        false -> Color(0xFFB91C1C).copy(alpha = 0.9f)
                                        else -> Color(0xFF334155).copy(alpha = 0.9f)
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = message,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (phoneNumber.isValidMpesaNumber()) {
                                    val formattedPhone = phoneNumber.toMpesaFormat()
                                    viewModel.initiatePayment(
                                        phone = formattedPhone,
                                        amount = amount.filter { it.isDigit() }
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            enabled = !paymentState.isLoading && phoneNumber.isValidMpesaNumber(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (paymentState.isLoading) "Processing..." else "Confirm Payment",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        if (paymentState.success == false) {
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = {
                                    viewModel.resetPaymentState()
                                    phoneNumber = ""
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                ),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                            ) {
                                Text("Try Again")
                            }
                        }

                        paymentState.transactionId?.let { transactionId ->
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = { viewModel.checkPaymentStatus(transactionId) },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !paymentState.isLoading,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                ),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = !paymentState.isLoading)
                            ) {
                                Text("Check Payment Status")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun String.isValidMpesaNumber(): Boolean {
    return when {
        matches("^07[0-9]{8}$".toRegex()) -> true
        matches("^254[0-9]{9}$".toRegex()) -> true
        else -> false
    }
}

private fun String.toMpesaFormat(): String {
    return when {
        startsWith("07") -> "254${substring(1)}"
        startsWith("254") -> this
        else -> this
    }
}