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
import com.example.haven.components.ParlorCard
import com.example.haven.model.MassageParlor
import com.example.haven.model.MassageType
import com.example.haven.R


@Composable
fun Homescreen() {
    var selectedMassageType by remember { mutableStateOf("") }

    val massageTypes = listOf(
        MassageType("Deep Tissue", R.drawable.ic_deep_tissue),
        MassageType("Swedish", R.drawable.ic_swedish),
        MassageType("Hot Stone", R.drawable.ic_hot_stone),
        MassageType("Shiatsu", R.drawable.ic_shiatsu),
        MassageType("Reflexology", R.drawable.ic_reflexology)
    )

    val massageParlors = listOf(
        MassageParlor(
            name = "Petit Lounge",
            offeredMassages = listOf("Deep Tissue", "Swedish", "Hot Stone"),
            rating = 4.8f,
            priceRange = "KSH 5000",
            imageResId = R.drawable.ic_petit_lounge
        ),
        MassageParlor(
            name = "Fairdeal Parlor",
            offeredMassages = listOf("Swedish", "Shiatsu", "Reflexology"),
            rating = 4.5f,
            priceRange = "KSH 7500",
            imageResId = R.drawable.ic_fairdeal_lounge
        ),
        MassageParlor(
            name = "Relax Haven",
            offeredMassages = listOf("Hot Stone", "Reflexology", "Deep Tissue"),
            rating = 4.7f,
            priceRange = "KSH 6500",
            imageResId = R.drawable.ic_relax_haven
        ),
        MassageParlor(
            name = "Rejuvenated Spa",
            offeredMassages = listOf("Shiatsu", "Swedish", "Reflexology"),
            rating = 4.9f,
            priceRange = "KSH 3500",
            imageResId = R.drawable.ic_rejuvenated_spa
        )
    )
    Column (modifier = Modifier.fillMaxSize()) {
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
            selectedMassageType = selectedMassageType
        )
    }
}

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
    selectedMassageType: String
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
                onBookClick = { /* Handle booking */ }
            )
        }
    }
}