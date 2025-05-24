package com.example.haven.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haven.components.ParlorCard
import com.example.haven.model.MassageParlor
import com.example.haven.model.MassageType
import com.example.haven.R
import java.net.URLEncoder
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun Homescreen(navController: NavController) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    var selectedMassageType by remember { mutableStateOf("") }
    var showProfile by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Select Massage Type",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            MassageTypeFilter(
                types = massageTypes,
                selectedType = selectedMassageType,
                onTypeSelected = { selectedMassageType = it }
            )

            ParlorList(
                parlors = massageParlors,
                selectedMassageType = selectedMassageType,
                onBookClick = { parlor ->
                    val encodedParlorName = URLEncoder.encode(parlor.name, "UTF-8")
                    val encodedMassageType = URLEncoder.encode(selectedMassageType, "UTF-8")
                    navController.navigate("receipt/${encodedParlorName}/${encodedMassageType}/${parlor.priceRange}")
                }
            )
        }
        ProfileSection(
            userName = currentUser?.displayName ?: "Guest",
            email = currentUser?.email ?: "Not logged in",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .zIndex(1f)
        )
    }
}

@Composable
private fun ProfileSection(
    userName: String,
    email: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Profile",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (expanded) {
            Card(
                modifier = Modifier
                    .offset(y = 48.dp)
                    .width(200.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


    val massageTypes = listOf(
        MassageType("Deep Tissue", R.drawable.ic_deep_tissue),
        MassageType("Swedish", R.drawable.ic_swedish),
        MassageType("Hot Stone", R.drawable.ic_hot_stone),
        MassageType("Shiatsu", R.drawable.ic_shiatsu),
        MassageType("Reflexology", R.drawable.ic_reflexology)
    )

    val massageParlors = listOf(
        MassageParlor(
            name = "Raddison Blu",
            offeredMassages = listOf("Deep Tissue", "Swedish", "Hot Stone","Shiatsu","Reflexology"),
            rating = 4.8f,
            priceRange = "KSH 25000",
            imageResIds = listOf(
                R.drawable.blu_spa,
                R.drawable.blu_2,
                R.drawable.blu_3,
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
                R.drawable.gem_3,
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
                R.drawable.gigiri_3,
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
                R.drawable.mokka_3,
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
                R.drawable.karen_3,
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
                R.drawable.maison_3,
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
                R.drawable.ololo_3,
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
                R.drawable.paradise_3,
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
                R.drawable.sahara_3,
            )
        )
    )


@Composable
private fun MassageTypeFilter(
    types: List<MassageType>,
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(types) { type ->
            FilterChip(
                selected = selectedType == type.name,
                onClick = {
                    onTypeSelected(if (selectedType == type.name) "" else type.name)
                },
                label = { Text(type.name, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = type.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun ParlorList(
    parlors: List<MassageParlor>,
    selectedMassageType: String,
    onBookClick: (MassageParlor) -> Unit
) {
    val filteredParlors = parlors.filter {
        selectedMassageType.isEmpty() || it.offeredMassages.contains(selectedMassageType)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(filteredParlors) { parlor ->
            ParlorCard(
                parlor = parlor,
                modifier = Modifier.fillMaxWidth(),
                onBookClick = { onBookClick(parlor) }
            )
        }
    }
}