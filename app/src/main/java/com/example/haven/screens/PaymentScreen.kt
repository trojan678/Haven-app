package com.example.haven.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    amount: String,
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    var phoneNumber by remember { mutableStateOf("") }
    val paymentState by viewModel.paymentState.collectAsState()
    var isLoading = paymentState.isLoading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("M-Pesa Payment") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                placeholder = { Text("07XXXXXXXX") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    autoCorrect = false
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))


            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            paymentState.message?.let { message ->
                Text(
                    text = message,
                    color = when {
                        paymentState.success == true -> MaterialTheme.colorScheme.primary
                        paymentState.success == false -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {
                    if (phoneNumber.isValidMpesaNumber()) {
                        viewModel.initiatePayment(
                            phone = phoneNumber,
                            amount = amount.filter { it.isDigit() }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isLoading && phoneNumber.isValidMpesaNumber()
            ) {
                Text(
                    text = if (isLoading) "Processing..." else "Confirm Payment",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


private fun String.isValidMpesaNumber(): Boolean {
    return matches("^07[0-9]{8}$".toRegex())
}