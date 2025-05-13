package com.example.haven.model

import androidx.annotation.DrawableRes

data class MassageParlor(
    val name: String,
    val offeredMassages: List<String>,
    val rating: Float,
    val priceRange: String,
    @DrawableRes val imageResId: Int
)