package com.example.haven.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLDecoder


@Composable
fun MessageReceiptScreen(
    parlorName: String?,
    massageType: String?,
    price: String?,
    navController: NavController,
    onBackClick: () -> Unit
) {
    val decodedParlor = URLDecoder.decode(parlorName ?: "", "UTF-8")
    val decodedMassage = URLDecoder.decode(massageType ?: "", "UTF-8")
    val formattedPrice = price ?: "N/A"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Booking Confirmation",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailRow("Massage Parlor:", decodedParlor)
            DetailRow("Massage Type:", decodedMassage)
            DetailRow("Total Price:", "KSH $formattedPrice")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Two buttons: Back and Confirm Booking
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Back button
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Back", fontSize = 16.sp)
            }

            // Confirm Booking button - Navigate to payment
            Button(
                onClick = {
                    // Extract only numbers from price for payment
                    val cleanPrice = formattedPrice.filter { it.isDigit() }
                    navController.navigate("payment/$cleanPrice")
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Confirm Booking", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}