package com.example.haven.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.haven.model.MassageParlor

@Composable
fun ParlorCard(
    parlor: MassageParlor,
    onBookClick: () -> Unit = {},
    modifier: Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column {
            Image(
                painter = painterResource(id = parlor.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                ) {
                    Text(
                        text = parlor.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    RatingBar(rating = parlor.rating)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Price Range: ${parlor.priceRange}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Offered Services:",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    parlor.offeredMassages.forEach { service ->
                        AssistChip(
                            onClick = {},
                            label = { Text(service) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onBookClick,
                    modifier = modifier
                ) {
                    Text("Book Now")
                }
            }
        }
    }
}

@Composable
private fun StarRating(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFD700)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "%.1f".format(rating))
    }
}