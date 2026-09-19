package com.example.EcoWise.GuidelinesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
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
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.R
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.ui.theme.*

@Composable
fun AboutUsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "About Us",
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Green_Mint_BG),
                        contentAlignment = Alignment.Center
                    ) {
                        EcoWiseIcon(
                            iconPainterResourceID = R.drawable.eco,
                            iconContentDescription = "EcoWise",
                            iconSize = 48.dp,
                            iconColor = Green_Forest
                        )
                    }

                    EcoWiseText(
                        textMsg = "EcoWise",
                        textSize = 28.sp,
                        textWeight = FontWeight.Bold,
                        textColor = TextPrimary
                    )

                    EcoWiseText(
                        textMsg = "v1.0.0",
                        textSize = 12.sp,
                        textColor = TextSecondary
                    )
                }
            }

            items(EcoWiseRepository.aboutUsSections, key = { it.id }) { section ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EcoWiseText(
                            textMsg = section.heading,
                            textSize = 16.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Dark
                        )

                        EcoWiseText(
                            textMsg = section.essay,
                            textSize = 13.sp,
                            textColor = TextSecondary
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Key Features",
                            textSize = 16.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Dark
                        )

                        FeatureItem(
                            icon = R.drawable.document_search,
                            title = "Product Analysis",
                            description = "Scan products to discover their environmental impact"
                        )

                        FeatureItem(
                            icon = R.drawable.filled_guide,
                            title = "Recycling Guide",
                            description = "Learn proper disposal methods for different materials"
                        )

                        FeatureItem(
                            icon = R.drawable.outlined_challenge,
                            title = "Eco Challenges",
                            description = "Complete daily challenges and earn eco points"
                        )

                        FeatureItem(
                            icon = R.drawable.filled_rewards_shop,
                            title = "Rewards Program",
                            description = "Redeem your points for sustainable products and services"
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Get in Touch",
                            textSize = 16.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Dark
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Green_Mint_BG),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.lightbulb,
                                    iconContentDescription = "Email",
                                    iconSize = 16.dp,
                                    iconColor = Green_Forest
                                )
                            }

                            Column {
                                EcoWiseText(
                                    textMsg = "Email",
                                    textSize = 12.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = TextPrimary
                                )
                                EcoWiseText(
                                    textMsg = "hello@ecowise.app",
                                    textSize = 11.sp,
                                    textColor = TextSecondary
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Green_Mint_BG),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.lightbulb,
                                    iconContentDescription = "Website",
                                    iconSize = 16.dp,
                                    iconColor = Green_Forest
                                )
                            }

                            Column {
                                EcoWiseText(
                                    textMsg = "Website",
                                    textSize = 12.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = TextPrimary
                                )
                                EcoWiseText(
                                    textMsg = "www.ecowise.app",
                                    textSize = 11.sp,
                                    textColor = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            EcoWiseText(
                                textMsg = "Made with ",
                                textSize = 13.sp,
                                textWeight = FontWeight.Medium,
                                textColor = TextSecondary
                            )
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.filled_heart,
                                iconContentDescription = "Heart",
                                iconSize = 14.dp,
                                iconColor = AlertRed
                            )
                            EcoWiseText(
                                textMsg = " for a greener planet",
                                textSize = 13.sp,
                                textWeight = FontWeight.Medium,
                                textColor = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.copyright,
                                iconContentDescription = "Copyright",
                                iconSize = 12.dp,
                                iconColor = TextSecondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            EcoWiseText(
                                textMsg = " 2024 EcoWise. All rights reserved.",
                                textSize = 11.sp,
                                textColor = TextSecondary
                            )
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
fun FeatureItem(
    icon: Int,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Green_Mint_BG),
            contentAlignment = Alignment.Center
        ) {
            EcoWiseIcon(
                iconPainterResourceID = icon,
                iconContentDescription = title,
                iconSize = 20.dp,
                iconColor = Green_Forest
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            EcoWiseText(
                textMsg = title,
                textSize = 13.sp,
                textWeight = FontWeight.Bold,
                textColor = TextPrimary
            )
            EcoWiseText(
                textMsg = description,
                textSize = 11.sp,
                textColor = TextSecondary
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnAboutUsScreen(){
    AboutUsScreen(
        navController = NavController(LocalContext.current)
    )
}