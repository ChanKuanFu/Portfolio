package com.example.EcoWise.module_2_RecyclingGuide

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.model.RecyclingCategoryGuide
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R


@Composable
fun RecyclingGuideScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route

    // get all guides from EcoWiseRepository
    val guides = EcoWiseRepository.recyclingGuides

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Recycling Guide",
        showBackButton = currentRoute != "home"
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            // Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    EcoWiseText(
                        textMsg = "Master the art of sorting",
                        textSize = 14.sp,
                        textColor = TextSecondary
                    )
                }
            }

            // show 2 categories in one row by Column Grid
            val rows = guides.chunked(2)
            items(rows.size) { rowIndex ->
                val pair = rows[rowIndex]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { guide ->
                        Box(modifier = Modifier.weight(1f)) {
                            RecyclingCategoryCard(
                                guide = guide,
                                onClick = {
                                    // navigate into specified category screen
                                    navController.navigate("EcoWise Recycling Category Information/${guide.id}")
                                }
                            )
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun RecyclingCategoryCard(
    guide: RecyclingCategoryGuide,
    onClick: () -> Unit
) {
    // 动态匹配图标与颜色
    val (iconRes, color) = when (guide.id) {
        "plastic" -> Pair(R.drawable.recycle, PlasticText)
        "paper" -> Pair(R.drawable.paper, PaperText)
        "glass" -> Pair(R.drawable.glass, Green_Forest)
        "metal" -> Pair(R.drawable.metal, MetalText)
        "organic" -> Pair(R.drawable.organic, Green_Bright)
        else -> Pair(R.drawable.electronic, PurpleText)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(138.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                EcoWiseIcon(
                    iconPainterResourceID = iconRes,
                    iconContentDescription = guide.title,
                    iconColor = color,
                    iconSize = 20.dp
                )
            }

            Column {
                EcoWiseText(
                    textMsg = guide.title,
                    textSize = 15.sp,
                    textWeight = FontWeight.Bold,
                    textColor = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                EcoWiseText(
                    textMsg = guide.summary,
                    textSize = 11.sp,
                    textColor = TextSecondary
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnRecyclingGuideScreen() {
    RecyclingGuideScreen(
        navController = rememberNavController(),
        modifier = Modifier
    )
}