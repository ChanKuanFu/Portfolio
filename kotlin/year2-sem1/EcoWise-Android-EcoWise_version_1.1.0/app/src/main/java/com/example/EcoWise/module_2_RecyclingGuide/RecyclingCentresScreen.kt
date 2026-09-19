package com.example.EcoWise.module_2_RecyclingGuide

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.EcoWise.model.RecyclingCentre
import com.example.EcoWise.ui.theme.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.example.EcoWise.R


@Composable
fun RecyclingCentresScreen(
    navController: NavController,
    initialCategoryFilter: String? = null,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route
    val allCentres by EcoWiseRepository.recyclingCentres.collectAsState()
    var selectedCategory by remember { mutableStateOf(initialCategoryFilter ?: "All") }
    var selectedCentreId by remember { mutableStateOf<String?>(allCentres.firstOrNull()?.id) }

    val categories = listOf("All", "Plastic", "Glass", "Paper", "Metal", "Electronics", "Organic")

    val filteredCentres = remember(selectedCategory, allCentres) {
        if (selectedCategory == "All") {
            allCentres
        } else {
            allCentres.filter { centre ->
                centre.acceptedMaterials.any { it.equals(selectedCategory, ignoreCase = true) }
            }
        }
    }

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Recycling Centres",
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG)
        ) {
            // Google Map View part
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(3.1390, 101.6869), 13f)
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                cameraPositionState = cameraPositionState
            ) {
                filteredCentres.forEach { centre ->

                    // Marker for each recycling centre
                    val markerState = rememberUpdatedMarkerState(
                        position = LatLng(centre.latitude, centre.longitude)
                    )

                    Marker(
                        state = markerState,
                        title = centre.name,
                        snippet = centre.address,
                        onClick = {
                            selectedCentreId = centre.id
                            true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // select category part
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = cat.equals(selectedCategory, ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Green_Forest else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) Green_Forest else CardBorder
                        ),
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        EcoWiseText(
                            textMsg = cat,
                            textSize = 13.sp,
                            textWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            textColor = if (isSelected) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recycling Centre & button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 70.dp) // 留出底部确认按钮的高度
                ) {
                    items(filteredCentres) { centre ->
                        val isSelected = centre.id == selectedCentreId

                        RecyclingCentreCard(
                            centre = centre,
                            isSaved = centre.isSaved,
                            isSelected = isSelected,
                            onCardClick = { selectedCentreId = centre.id },
                            onToggleSave = {
                                EcoWiseRepository.toggleSaveCentre(centre.id)
                            }
                        )
                    }
                }

                // Confirm button & pass selected centre id to result screen
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.Transparent
                ) {
                    Button(
                        onClick = {
                            selectedCentreId?.let { id ->
                                navController.navigate("EcoWise Recycling Drop-offs Center Result/$id")
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green_Primary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Confirm Selected Centre",
                            textSize = 15.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecyclingCentreCard(
    centre: RecyclingCentre,
    isSaved: Boolean,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onToggleSave: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Green_Ultra_Light else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) Green_Forest else CardBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    EcoWiseText(
                        textMsg = centre.name,
                        textSize = 15.sp,
                        textWeight = FontWeight.Bold,
                        textColor = TextPrimary
                    )
                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.size(24.dp)
                    ) {
                        EcoWiseIcon(
                            iconPainterResourceID = if (isSaved) R.drawable.filled_star else R.drawable.outlined_star,
                            iconContentDescription = "Save centre",
                            iconColor = if (isSaved) StarYellow else TextMuted,
                            iconSize = 18.dp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (centre.isOpen) Green_Mint_BG else AlertRedLight
                ) {
                    EcoWiseText(
                        textMsg = if (centre.isOpen) "Open" else "Closed",
                        textSize = 11.sp,
                        textWeight = FontWeight.Bold,
                        textColor = if (centre.isOpen) Green_Forest else AlertRed,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                EcoWiseIcon(
                    iconPainterResourceID = R.drawable.location,
                    iconContentDescription = null,
                    iconColor = TextMuted,
                    iconSize = 15.dp
                )
                EcoWiseText(
                    textMsg = "${centre.distanceKm} km away • ${centre.address}",
                    textSize = 12.sp,
                    textColor = TextSecondary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                centre.acceptedMaterials.forEach { mat ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        EcoWiseText(
                            textMsg = mat,
                            textSize = 11.sp,
                            textWeight = FontWeight.Medium,
                            textColor = TextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnRecyclingCentresScreen() {
    RecyclingCentresScreen(
        navController = rememberNavController()
    )
}