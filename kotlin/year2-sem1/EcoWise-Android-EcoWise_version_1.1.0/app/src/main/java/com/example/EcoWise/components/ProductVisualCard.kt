package com.example.EcoWise.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProductVisualCard(
    visualType: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF3F6F1)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        when (visualType) {
            "oat_milk" -> Product_OatMilk(Modifier.fillMaxSize().padding(12.dp))
            "water_bottle" -> Product_WaterBottle(Modifier.fillMaxSize().padding(12.dp))
            "coffee" -> Product_CoffeePack(Modifier.fillMaxSize().padding(12.dp))
            "styrofoam" -> Product_Polystyrene(Modifier.fillMaxSize().padding(12.dp))
            "bottle" -> Product_ReusableBottle(Modifier.fillMaxSize().padding(12.dp))
            "tote" -> Product_ToteBag(Modifier.fillMaxSize().padding(12.dp))
            else -> Product_EcoFriendly(Modifier.fillMaxSize().padding(12.dp))
        }
    }
}