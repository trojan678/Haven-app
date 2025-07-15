package com.example.haven.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.haven.model.MassageParlor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ExperimentalLayoutApi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ParlorCard(
    parlor: MassageParlor,
    onBookClick: () -> Unit = {},
    modifier: Modifier
) {
    val lazyListState = rememberLazyListState()
    var currentImageIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }) {
        currentImageIndex = lazyListState.firstVisibleItemIndex
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                LazyRow(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(parlor.imageResIds) { imageResId ->
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(parlor.imageResIds.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    color = if (index == currentImageIndex)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                        )
                    }
                }
            }


            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = parlor.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    StarRating(rating = parlor.rating)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Price Range: ${parlor.priceRange}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Offered Packages:",
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onBookClick,
                    modifier = Modifier.fillMaxWidth()
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