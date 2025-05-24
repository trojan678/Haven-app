package com.example.haven.model


data class MassageParlor(
    val name: String,
    val offeredMassages: List<String>,
    val rating: Float,
    val priceRange: String,
    val imageResIds: List<Int>
)