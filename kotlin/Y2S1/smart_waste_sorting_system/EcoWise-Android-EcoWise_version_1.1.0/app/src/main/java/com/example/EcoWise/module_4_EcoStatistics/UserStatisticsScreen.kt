package com.example.EcoWise.module_4_EcoStatistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R


@Composable
fun UserStatisticsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route

    // get user stats from EcoWiseRepository
    val stats by EcoWiseRepository.userStats.collectAsState()

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "My Statistics",
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //  Eco Points & Average Eco Score
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Eco Points Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp, horizontal = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.filled_star,
                                iconContentDescription = null,
                                iconColor = StarYellow,
                                iconSize = 26.dp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            EcoWiseText(
                                textMsg = "${stats.ecoPoints}",
                                textSize = 24.sp,
                                textWeight = FontWeight.Bold,
                                textColor = StarYellow
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            EcoWiseText(
                                textMsg = "Eco Points",
                                textSize = 12.sp,
                                textColor = TextSecondary
                            )
                        }
                    }

                    // Average Eco Score Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp, horizontal = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.eco,
                                iconContentDescription = null,
                                iconColor = Green_Forest,
                                iconSize = 26.dp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            EcoWiseText(
                                textMsg = "${stats.avgEcoScore}",
                                textSize = 24.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Green_Forest
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            EcoWiseText(
                                textMsg = "Average Eco Score",
                                textSize = 12.sp,
                                textColor = TextSecondary
                            )
                        }
                    }
                }
            }

            // "Your Activity" Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Your Activity",
                            textSize = 18.sp,
                            textWeight = FontWeight.Bold,
                            textColor = TextPrimary
                        )
                        EcoWiseText(
                            textMsg = "Previous activity done",
                            textSize = 13.sp,
                            textColor = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        StatBulletRow(
                            label = "Product Analysed",
                            value = "${stats.productsAnalysed}",
                            bulletColor = Green_Forest
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        StatBulletRow(
                            label = "Saved Recycling Centres",
                            value = "${stats.savedCentresCount}",
                            bulletColor = Green_Dark
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        StatBulletRow(
                            label = "Challenges Completed",
                            value = "${stats.challengesCompleted}",
                            bulletColor = Color(0xFFA5D6A7)
                        )
                    }
                }
            }

            // "Analyse by Category" Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Analyse by Category",
                            textSize = 18.sp,
                            textWeight = FontWeight.Bold,
                            textColor = TextPrimary
                        )
                        EcoWiseText(
                            textMsg = "Product types analysed",
                            textSize = 13.sp,
                            textColor = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        stats.categoryStats.forEachIndexed { index, (catName, count) ->
                            val dotColor = when (index) {
                                0 -> Green_Forest
                                1 -> Green_Mint
                                2 -> Green_Dark
                                3 -> Color(0xFFA5D6A7)
                                else -> Color(0xFF81C784)
                            }
                            StatBulletRow(
                                label = catName,
                                value = "$count",
                                bulletColor = dotColor
                            )
                            if (index < stats.categoryStats.size - 1) {
                                Spacer(modifier = Modifier.height(14.dp))
                            }
                        }
                    }
                }
            }

            // "This Month Activity" Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "This Month Activity",
                            textSize = 18.sp,
                            textWeight = FontWeight.Bold,
                            textColor = TextPrimary
                        )
                        EcoWiseText(
                            textMsg = "Activity done this month",
                            textSize = 13.sp,
                            textColor = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EcoWiseText(
                                textMsg = "Eco Points Earned",
                                textSize = 15.sp,
                                textWeight = FontWeight.Normal,
                                textColor = TextPrimary
                            )
                            EcoWiseText(
                                textMsg = "${stats.monthPoints}",
                                textSize = 16.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Green_Forest
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EcoWiseText(
                                textMsg = "Product Analysed",
                                textSize = 15.sp,
                                textWeight = FontWeight.Normal,
                                textColor = TextPrimary
                            )
                            EcoWiseText(
                                textMsg = "${stats.monthProducts}",
                                textSize = 16.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Green_Forest
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EcoWiseText(
                                textMsg = "Challenges Completed",
                                textSize = 15.sp,
                                textWeight = FontWeight.Normal,
                                textColor = TextPrimary
                            )
                            EcoWiseText(
                                textMsg = "${stats.monthChallenges}",
                                textSize = 16.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Green_Forest
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBulletRow(
    label: String,
    value: String,
    bulletColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(bulletColor)
            )
            EcoWiseText(
                textMsg = label,
                textSize = 15.sp,
                textColor = TextPrimary
            )
        }

        EcoWiseText(
            textMsg = value,
            textSize = 15.sp,
            textWeight = FontWeight.Bold,
            textColor = Green_Forest
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnUserStatisticsScreen(){
    UserStatisticsScreen(
        navController = rememberNavController()
    )
}