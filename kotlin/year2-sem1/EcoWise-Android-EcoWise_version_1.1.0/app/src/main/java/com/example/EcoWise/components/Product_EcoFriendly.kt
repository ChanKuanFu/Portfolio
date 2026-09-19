package com.example.EcoWise.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.EcoWise.R
import com.example.EcoWise.ui.theme.Green_Primary

@Composable
fun Product_EcoFriendly(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Icon(
            painter = painterResource(id = R.drawable.eco),
            contentDescription = "Eco",
            tint = Green_Primary,
            modifier = Modifier.size(36.dp)
        )
    }
}
