package com.example.EcoWise.module_3_EcoChallenge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import kotlinx.coroutines.launch
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.components.ProductVisualCard
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R


@Composable
fun ChallengeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route
    val stats by EcoWiseRepository.userStats.collectAsState()
    val challenges by EcoWiseRepository.challenges.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var redeemFeedback by remember { mutableStateOf<String?>(null) }

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Eco Challenges",
        showBackButton = currentRoute != "home"
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EcoWiseText(
                        textMsg = "Active Challenges",
                        textSize = 20.sp,
                        textWeight = FontWeight.Bold,
                        textColor = TextPrimary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.6f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        EcoWiseIcon(
                            iconPainterResourceID = R.drawable.filled_star,
                            iconContentDescription = "Points",
                            iconColor = StarYellow,
                            iconSize = 16.dp
                        )
                        EcoWiseText(
                            textMsg = "${stats.ecoPoints} pts",
                            textSize = 14.sp,
                            textWeight = FontWeight.Bold,
                            textColor = TextPrimary
                        )
                    }
                }
            }

            items(challenges) { challenge ->
                ActiveChallengeCard(
                    challenge = challenge,
                    onClick = {
                        navController.navigate("Challenge Progress Upload/${challenge.id}")
                    }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EcoWiseText(
                        textMsg = "Reward Shop",
                        textSize = 20.sp,
                        textWeight = FontWeight.Bold,
                        textColor = TextPrimary
                    )

                    EcoWiseText(
                        textMsg = "Explore More",
                        textSize = 14.sp,
                        textWeight = FontWeight.Bold,
                        textColor = Green_Forest,
                        modifier = Modifier
                            .clickable { navController.navigate("EcoWise Rewards Shop") }
                            .padding(vertical = 4.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    EcoWiseRepository.shopRewards.forEach { reward ->
                        ShopRewardItemCard(
                            reward = reward,
                            userPoints = stats.ecoPoints,
                            onRedeem = {
                                if (stats.ecoPoints >= reward.pointsCost) {
                                    coroutineScope.launch {
                                        EcoWiseRepository.redeemReward(reward)
                                        redeemFeedback = "Redeemed '${reward.title}'! Added to My Rewards."
                                    }
                                } else {
                                    redeemFeedback = "Not enough points. Need ${reward.pointsCost - stats.ecoPoints} more points."
                                }
                            }
                        )
                    }
                }
            }

            if (redeemFeedback != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Green_Ultra_Light,
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.eco,
                                iconContentDescription = null,
                                iconColor = Green_Forest,
                                iconSize = 20.dp
                            )
                            EcoWiseText(
                                textMsg = redeemFeedback ?: "",
                                textSize = 13.sp,
                                textWeight = FontWeight.SemiBold,
                                textColor = Green_Dark,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveChallengeCard(
    challenge: com.example.EcoWise.model.EcoChallenge,
    onClick: () -> Unit
) {
    val progress = (challenge.currentProgress.toFloat() / challenge.totalProgress.toFloat()).coerceIn(0f, 1f)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Green_Mint_BG
                ) {
                    EcoWiseText(
                        textMsg = challenge.statusBadge,
                        textSize = 11.sp,
                        textWeight = FontWeight.Bold,
                        textColor = Green_Dark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                EcoWiseText(
                    textMsg = "${challenge.currentProgress}/${challenge.totalProgress} ${challenge.unit}",
                    textSize = 13.sp,
                    textColor = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    EcoWiseText(
                        textMsg = challenge.title,
                        textSize = 18.sp,
                        textWeight = FontWeight.Bold,
                        textColor = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    EcoWiseText(
                        textMsg = challenge.description,
                        textSize = 13.sp,
                        textColor = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Green_Forest),
                    contentAlignment = Alignment.Center
                ) {
                    EcoWiseIcon(
                        iconPainterResourceID = R.drawable.arrowforward,
                        iconContentDescription = "Add progress",
                        iconColor = Color.White,
                        iconSize = 20.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Green_Forest,
                trackColor = Color(0xFFE5ECE5)
            )
        }
    }
}

@Composable
fun ShopRewardItemCard(
    reward: com.example.EcoWise.model.ShopReward,
    userPoints: Int,
    onRedeem: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.width(175.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF3F6F1))
            ) {
                ProductVisualCard(
                    visualType = reward.imageType,
                    modifier = Modifier.fillMaxSize()
                )

                if (reward.discountBadge != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                    ) {
                        EcoWiseText(
                            textMsg = reward.discountBadge,
                            textSize = 10.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Forest,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            EcoWiseText(
                textMsg = reward.title,
                textSize = 15.sp,
                textWeight = FontWeight.Bold,
                textColor = TextPrimary
            )

            EcoWiseText(
                textMsg = reward.subtitle,
                textSize = 12.sp,
                textColor = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onRedeem,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green_Forest,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                EcoWiseText(
                    textMsg = "${reward.pointsCost} Points",
                    textSize = 13.sp,
                    textWeight = FontWeight.Bold,
                    textColor = Color.White
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnChallengeScreen(){
    ChallengeScreen(
        navController = rememberNavController()
    )
}