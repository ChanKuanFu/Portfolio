package com.example.EcoWise.module_4_EcoStatistics

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.model.ShopReward
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R


@Composable
fun RewardsShopScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route

    // "All" plus whatever categories the repository's real reward catalog
    // actually uses, so the filter chips can't drift out of sync with it.
    val categories = listOf("All") + EcoWiseRepository.rewardCategories
    var selectedCategory by remember { mutableStateOf("All") }

    // get current context
    val context = LocalContext.current

    // get current points from EcoWiseRepository
    val userStats by EcoWiseRepository.userStats.collectAsState()
    val currentPoints = userStats.ecoPoints
    val coroutineScope = rememberCoroutineScope()

    // Real reward catalog from the repository — previously this screen had
    // its own separate hardcoded RW1-RW8 list that had no connection to
    // EcoWiseRepository.redeemReward(), so tapping a card could never
    // actually redeem anything.
    val rewards = EcoWiseRepository.shopRewards

    val filteredRewards = if (selectedCategory == "All") {
        rewards
    } else {
        rewards.filter { it.rewardsCategory == selectedCategory }
    }

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Rewards Shop",
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
            // Header Points Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Green_Deep_Navy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            EcoWiseText(
                                textMsg = "Your Points",
                                textSize = 12.sp,
                                textColor = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            EcoWiseText(
                                textMsg = "$currentPoints pts",
                                textSize = 22.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.filled_star,
                                iconContentDescription = "Points",
                                iconSize = 24.dp,
                                iconColor = StarYellow
                            )
                        }
                    }
                }
            }

            // Category Filter
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        val isSelected = category == selectedCategory
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Green_Forest else Color.White
                            ),
                            modifier = Modifier
                                .clickable { selectedCategory = category }
                        ) {
                            EcoWiseText(
                                textMsg = category,
                                textSize = 12.sp,
                                textWeight = FontWeight.Bold,
                                textColor = if (isSelected) Color.White else TextPrimary,
                                textPadding = 10.dp
                            )
                        }
                    }
                }
            }

            // Rewards Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    filteredRewards.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { reward ->
                                RewardCard(
                                    reward = reward,
                                    onClick = {
                                        coroutineScope.launch {
                                            val success = EcoWiseRepository.redeemReward(reward)
                                            val message = if (success) {
                                                "Redeemed: ${reward.title}"
                                            } else {
                                                "Not enough points for ${reward.title}"
                                            }
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun RewardCard(
    reward: ShopReward,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .height(200.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            com.example.EcoWise.components.ProductVisualCard(
                visualType = reward.imageType,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                EcoWiseText(
                    textMsg = reward.title,
                    textSize = 12.sp,
                    textWeight = FontWeight.Bold,
                    textColor = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        EcoWiseIcon(
                            iconPainterResourceID = R.drawable.filled_star,
                            iconContentDescription = "Cost",
                            iconSize = 12.dp,
                            iconColor = StarYellow
                        )
                        EcoWiseText(
                            textMsg = "${reward.pointsCost}",
                            textSize = 11.sp,
                            textWeight = FontWeight.Bold,
                            textColor = TextPrimary
                        )
                    }

                    EcoWiseIcon(
                        iconPainterResourceID = R.drawable.arrowforward,
                        iconContentDescription = "Redeem",
                        iconSize = 14.dp,
                        iconColor = Green_Forest
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnRewardsShopScreen(){
    RewardsShopScreen(
        navController = rememberNavController()
    )
}
