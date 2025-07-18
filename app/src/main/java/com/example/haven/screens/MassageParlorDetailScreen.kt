package com.example.haven.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.haven.R
import com.example.haven.model.MassageParlor

data class MassagePackage(
    val massageType: String,
    val duration: String,
    val price: String,
    val description: String,
    val iconRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MassageParlorDetailScreen(
    parlorName: String,
    navController: NavController
) {
    val parlor = getMassageParlors().find { it.name == parlorName }

    if (parlor == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    var selectedPackage by remember { mutableStateOf<MassagePackage?>(null) }
    val packages = getPackagesForParlor(parlor)

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
                    title = { Text(parlor.name, color = Color.White) },
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image Gallery
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF334155).copy(alpha = 0.9f)
                        )
                    ) {
                        LazyRow(
                            modifier = Modifier.height(220.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(parlor.imageResIds) { imageRes ->
                                AsyncImage(
                                    model = imageRes,
                                    contentDescription = "${parlor.name} image",
                                    modifier = Modifier
                                        .width(300.dp)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(id = imageRes),
                                    error = painterResource(id = imageRes),
                                    fallback = painterResource(id = imageRes)
                                )
                            }
                        }
                    }
                }

                // Parlor Info - Updated with dark color scheme
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E3A8A).copy(alpha = 0.9f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = parlor.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${parlor.rating}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Price Range: ${parlor.priceRange}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Available Packages
                item {
                    Text(
                        text = "Available Massage Packages",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.White
                    )
                }

                items(packages) { massagePackage ->
                    PackageCard(
                        massagePackage = massagePackage,
                        isSelected = selectedPackage == massagePackage,
                        onSelect = {
                            selectedPackage = if (selectedPackage == massagePackage) null else massagePackage
                        }
                    )
                }

                // Proceed to Checkout Button
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            selectedPackage?.let { massagePackage ->
                                val amount = massagePackage.price.filter { it.isDigit() }
                                navController.navigate("payment/$amount")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = selectedPackage != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981).copy(alpha = 0.9f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (selectedPackage != null) {
                                "Proceed to Checkout - ${selectedPackage!!.price}"
                            } else {
                                "Select a Package"
                            },
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PackageCard(
    massagePackage: MassagePackage,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                Color(0xFF1E3A8A) // Dark blue when selected
            else
                Color(0xFF334155) // Dark slate gray when not selected
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with contrasting background
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        if (isSelected) Color(0xFF3B82F6).copy(alpha = 0.3f)
                        else Color(0xFF64748B).copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = massagePackage.iconRes),
                    contentDescription = massagePackage.massageType,
                    modifier = Modifier
                        .size(40.dp)
                        .scale(1.1f),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = massagePackage.massageType,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = massagePackage.duration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = massagePackage.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = massagePackage.price,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10B981) // Emerald green for price
            )
        }
    }
}

// Helper function to generate packages for each parlor
private fun getPackagesForParlor(parlor: MassageParlor): List<MassagePackage> {
    val packages = mutableListOf<MassagePackage>()

    parlor.offeredMassages.forEach { massageType ->
        val packageInfo = when (massageType) {
            "Deep Tissue" -> MassagePackage(
                massageType = massageType,
                duration = "90 minutes",
                price = "KSH 8,500",
                description = "Intense pressure massage targeting deep muscle layers",
                iconRes = R.drawable.ic_deep_tissue
            )
            "Swedish" -> MassagePackage(
                massageType = massageType,
                duration = "60 minutes",
                price = "KSH 6,500",
                description = "Relaxing massage with long, flowing strokes",
                iconRes = R.drawable.ic_swedish
            )
            "Hot Stone" -> MassagePackage(
                massageType = massageType,
                duration = "75 minutes",
                price = "KSH 9,500",
                description = "Therapeutic massage using heated stones",
                iconRes = R.drawable.ic_hot_stone
            )
            "Shiatsu" -> MassagePackage(
                massageType = massageType,
                duration = "60 minutes",
                price = "KSH 7,500",
                description = "Japanese pressure point massage technique",
                iconRes = R.drawable.ic_shiatsu
            )
            "Reflexology" -> MassagePackage(
                massageType = massageType,
                duration = "45 minutes",
                price = "KSH 5,500",
                description = "Foot massage targeting pressure points",
                iconRes = R.drawable.ic_reflexology
            )
            else -> MassagePackage(
                massageType = massageType,
                duration = "60 minutes",
                price = "KSH 6,500",
                description = "Professional massage therapy",
                iconRes = R.drawable.ic_swedish
            )
        }
        packages.add(packageInfo)
    }
    return packages
}

// Function to get massage parlors data
private fun getMassageParlors() = listOf(
    MassageParlor(
        name = "Raddison Blu",
        offeredMassages = listOf("Deep Tissue", "Swedish", "Hot Stone", "Shiatsu", "Reflexology"),
        rating = 4.8f,
        priceRange = "KSH 25000",
        imageResIds = listOf(
            R.drawable.blu_spa,
            R.drawable.blu_2,
            R.drawable.blu_3
        )
    ),
    MassageParlor(
        name = "Gem Forest",
        offeredMassages = listOf("Swedish", "Shiatsu", "Reflexology"),
        rating = 4.2f,
        priceRange = "KSH 15000",
        imageResIds = listOf(
            R.drawable.gem_1,
            R.drawable.gem_2,
            R.drawable.gem_3
        )
    ),
    MassageParlor(
        name = "Gigiri Lions Villa",
        offeredMassages = listOf("Hot Stone", "Reflexology", "Deep Tissue"),
        rating = 4.1f,
        priceRange = "KSH 17500",
        imageResIds = listOf(
            R.drawable.gigiri_1,
            R.drawable.gigiri_2,
            R.drawable.gigiri_3
        )
    ),
    MassageParlor(
        name = "Hotel Mokka City",
        offeredMassages = listOf("Shiatsu", "Swedish", "Reflexology"),
        rating = 3.5f,
        priceRange = "KSH 8500",
        imageResIds = listOf(
            R.drawable.mokka_1,
            R.drawable.mokka_2,
            R.drawable.mokka_3
        )
    ),
    MassageParlor(
        name = "Karen Gables",
        offeredMassages = listOf("Deep Tissue", "Hot Stone"),
        rating = 3.9f,
        priceRange = "KSH 38000",
        imageResIds = listOf(
            R.drawable.karen_1,
            R.drawable.karen_2,
            R.drawable.karen_3
        )
    ),
    MassageParlor(
        name = "La Maison Royale",
        offeredMassages = listOf("Swedish", "Shiatsu", "Reflexology"),
        rating = 4.5f,
        priceRange = "KSH 6900",
        imageResIds = listOf(
            R.drawable.maison_1,
            R.drawable.maison_2,
            R.drawable.maison_3
        )
    ),
    MassageParlor(
        name = "Ololo Hotel",
        offeredMassages = listOf("Swedish", "Hot Stone"),
        rating = 2.3f,
        priceRange = "KSH 5000",
        imageResIds = listOf(
            R.drawable.ololo_1,
            R.drawable.ololo_2,
            R.drawable.ololo_3
        )
    ),
    MassageParlor(
        name = "Palacina The Residence",
        offeredMassages = listOf("Deep Tissue", "Swedish"),
        rating = 3.9f,
        priceRange = "KSH 12000",
        imageResIds = listOf(
            R.drawable.paradise_1,
            R.drawable.paradise_2,
            R.drawable.paradise_3
        )
    ),
    MassageParlor(
        name = "Sahara West",
        offeredMassages = listOf("Reflexology"),
        rating = 4.0f,
        priceRange = "KSH 3500",
        imageResIds = listOf(
            R.drawable.sahara_1,
            R.drawable.sahara_2,
            R.drawable.sahara_3
        )
    )
)