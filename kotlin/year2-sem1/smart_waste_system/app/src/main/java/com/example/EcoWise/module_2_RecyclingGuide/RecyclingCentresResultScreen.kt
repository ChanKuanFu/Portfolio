package com.example.EcoWise.module_2_RecyclingGuide

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch


@Composable
fun RecyclingCentresResultScreen(
    navController: NavController,
    centreId: String,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route
    // get current screen context for Toast
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // get all centres from EcoWiseRepository
    val allCentres by EcoWiseRepository.recyclingCentres.collectAsState()
    val selectedCentre = allCentres.find { it.id == centreId } ?: allCentres.firstOrNull()

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Recycling Turn-in Result",
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
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Green_Light_BG),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.successfully,
                                iconContentDescription = null,
                                iconSize = 24.dp,
                                iconColor = Green_Forest
                            )
                            EcoWiseText(
                                textMsg = "Drop-off Selected Successfully!",
                                textSize = 18.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Green_Forest
                            )
                        }

                        EcoWiseText(
                            textMsg = "Your product and selected recycling hub have been successfully matched.",
                            textSize = 13.sp,
                            textColor = TextSecondary
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Green_Light_BG),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Selected Recycling Centre",
                            textSize = 15.sp,
                            textWeight = FontWeight.Bold,
                            textColor = TextPrimary
                        )

                        Row(
                            modifier = Modifier
                                .clickable(onClick = {
                                    Toast.makeText(
                                        context,
                                        "This is Centre Name.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        ){
                            OutlinedTextField(
                                value = selectedCentre?.name ?: "Unknown Centre",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Centre Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Row(
                            modifier = Modifier
                                .clickable(onClick = {
                                    Toast.makeText(
                                        context,
                                        "This is address for Centre.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        ){
                            OutlinedTextField(
                                value = selectedCentre?.address ?: "No address available",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Centre Address") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }


                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCentre?.latitude?.toString() ?: "0.0",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Latitude") },
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(onClick = {
                                        Toast.makeText(
                                            context,
                                            "Latitude.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                            )
                            OutlinedTextField(
                                value = selectedCentre?.longitude?.toString() ?: "0.0",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Longitude") },
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(onClick = {
                                        Toast.makeText(
                                            context,
                                            "Longitude.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                            )
                        }
                    }
                }
            }

            // Turn in & Back to Home Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    //  Turn in Button for pass in recycling activity
                    Button(
                        onClick = {
                            selectedCentre?.let { centre ->
                                scope.launch {
                                    EcoWiseRepository.saveRecyclingActivity(centre)
                                    // show toast msg
                                    Toast.makeText(
                                        context,
                                        "Activity turned in successfully!!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("EcoWise Recycling Turn-in Result")
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green_Forest,
                            contentColor = SecondaryActionButton
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Turn in",
                            textSize = 14.sp,
                            textWeight = FontWeight.Bold,
                            textColor = SecondaryActionButton
                        )
                    }

                    // Cancel button
                    Button(
                        onClick = {
                            Toast.makeText(
                                    context,
                                "Cancelling process...",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DisagreeButton,
                            contentColor = AgreeText
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            // UI
                            EcoWiseText(
                                textMsg = "Cancel",
                                textSize = 14.sp,
                                textWeight = FontWeight.Bold,
                                textColor = AgreeText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewRecyclingCentresResultScreen() {
    RecyclingCentresResultScreen(
        navController = rememberNavController(),
        centreId = "centre-1"
    )
}