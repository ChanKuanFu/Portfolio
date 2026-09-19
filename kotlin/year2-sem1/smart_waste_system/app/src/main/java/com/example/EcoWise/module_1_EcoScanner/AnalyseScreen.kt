package com.example.EcoWise.module_1_EcoScanner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.RoomDB.EcoWiseViewModel
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyseScreen(
    navController: NavController,
    initialCategory: String? = null, // get category from RecyclingGuide screen
    modifier: Modifier = Modifier,
    viewModel: EcoWiseViewModel = viewModel() //  EcoWiseViewModel to synchronize Room DB & Supabase
) {
    val currentRoute = navController.currentDestination?.route

    // if initialCategory is null then set default value
    var category by remember { mutableStateOf(initialCategory ?: "") }
    var productName by remember { mutableStateOf("") }
    var brandName by remember { mutableStateOf("") }
    var weightSize by remember { mutableStateOf("") }
    var packagingMaterial by remember { mutableStateOf("") }
    var isOrganic by remember { mutableStateOf(false) }
    var selectedEcoLabels by remember { mutableStateOf(emptySet<String>()) }

    // validation
    var categoryError by remember { mutableStateOf(false) }
    var productNameError by remember { mutableStateOf(false) }
    var packagingMaterialError by remember { mutableStateOf(false) }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    // dropdown menu
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showMaterialDropdown by remember { mutableStateOf(false) }
    var showOrganicDropdown by remember { mutableStateOf(false) }

    val categories = listOf(
        "Food & Beverages",
        "Household Products",
        "Personal Care",
        "Electronics",
        "Clothing",
        "Plastic",
        "Paper",
        "Glass",
        "Metal",
        "Organic",
        "E-Waste"
    )

    val materials = listOf(
        "Plastic",
        "Paper",
        "Glass",
        "Metal",
        "Electronics",
        "Biodegradable"
    )

    val ecoLabelOptions = listOf(
        "Organic",
        "FSC Certified",
        "Fair Trade",
        "Recyclable",
        "BPA Free"
    )

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Product Information",
        showBackButton = currentRoute != "home",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EcoWiseText(
                        textMsg = "Fill in product details to analyze its \nenvironmental impact",
                        textSize = 13.sp,
                        textColor = TextSecondary
                    )

                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("Analyse History") }
                            .padding(4.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "View Recent History",
                            textSize = 14.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Forest
                        )
                    }
                }
            }

            // show error
            if (hasAttemptedSubmit && (categoryError || productNameError || packagingMaterialError)) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AlertRedLight),
                        border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.warning,
                                iconContentDescription = null,
                                iconSize = 14.dp,
                                iconColor = NoticeYellow
                            )
                            EcoWiseText(
                                textMsg = " Please fill in all required fields marked below.",
                                textSize = 13.sp,
                                textWeight = FontWeight.SemiBold,
                                textColor = AlertRed
                            )
                        }
                    }
                }
            }

            //  Product Category Dropdown
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputFieldLabel(text = "Product Category")
                        EcoWiseText(
                            textMsg = " *",
                            textSize = 14.sp,
                            textColor = AlertRed,
                            textWeight = FontWeight.Bold
                        )
                    }

                    Box {
                        OutlinedCard(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, if (categoryError) AlertRed else CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clickable {
                                    showCategoryDropdown = true
                                    categoryError = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                EcoWiseText(
                                    textMsg = category.ifBlank { "Select a Category" },
                                    textSize = 15.sp,
                                    textColor = if (category.isNotBlank()) TextPrimary else TextMuted
                                )
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.arrow_down,
                                    iconContentDescription = "Dropdown",
                                    iconSize = 16.dp,
                                    iconColor = if (categoryError) AlertRed else TextSecondary
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        category = cat
                                        categoryError = false
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    if (categoryError) {
                        EcoWiseText(
                            textMsg = "Please select a product category",
                            textSize = 12.sp,
                            textColor = AlertRed,
                            textWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }
            }

            //  Product Name (Required)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputFieldLabel(text = "Product Name")
                        EcoWiseText(
                            textMsg = " *",
                            textSize = 14.sp,
                            textColor = AlertRed,
                            textWeight = FontWeight.Bold
                        )
                    }

                    OutlinedTextField(
                        value = productName,
                        onValueChange = {
                            productName = it
                            if (it.isNotBlank()) productNameError = false
                        },
                        placeholder = { EcoWiseText(textMsg = "Enter product name", textSize = 15.sp, textColor = TextMuted) },
                        shape = RoundedCornerShape(16.dp),
                        isError = productNameError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            errorContainerColor = Color.White,
                            focusedBorderColor = Green_Forest,
                            unfocusedBorderColor = CardBorder,
                            errorBorderColor = AlertRed,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (productNameError) {
                        EcoWiseText(
                            textMsg = "Please enter the product name",
                            textSize = 12.sp,
                            textColor = AlertRed,
                            textWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }
            }

            //  Brand Name
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    InputFieldLabel(text = "Brand Name (Optional)")
                    OutlinedTextField(
                        value = brandName,
                        onValueChange = { brandName = it },
                        placeholder = { EcoWiseText(textMsg = "Enter brand name", textSize = 15.sp, textColor = TextMuted) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Green_Forest,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            //  Product Weight / Size
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    InputFieldLabel(text = "Product Weight / Size (Optional)")
                    OutlinedTextField(
                        value = weightSize,
                        onValueChange = { weightSize = it },
                        placeholder = { EcoWiseText(textMsg = "e.g. 500g, 1L, 250ml", textSize = 15.sp, textColor = TextMuted) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Green_Forest,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            //  Packaging Material Dropdown
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputFieldLabel(text = "Packaging Material")
                        EcoWiseText(
                            textMsg = " *",
                            textSize = 14.sp,
                            textColor = AlertRed,
                            textWeight = FontWeight.Bold
                        )
                    }

                    Box {
                        OutlinedCard(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, if (packagingMaterialError) AlertRed else CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clickable {
                                    showMaterialDropdown = true
                                    packagingMaterialError = false
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                EcoWiseText(
                                    textMsg = packagingMaterial.ifBlank { "Select a Material" },
                                    textSize = 15.sp,
                                    textColor = if (packagingMaterial.isNotBlank()) TextPrimary else TextMuted
                                )
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.arrow_down,
                                    iconContentDescription = "Dropdown",
                                    iconSize = 16.dp,
                                    iconColor = if (packagingMaterialError) AlertRed else TextSecondary
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showMaterialDropdown,
                            onDismissRequest = { showMaterialDropdown = false }
                        ) {
                            materials.forEach { mat ->
                                DropdownMenuItem(
                                    text = { Text(mat) },
                                    onClick = {
                                        packagingMaterial = mat
                                        packagingMaterialError = false
                                        showMaterialDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    if (packagingMaterialError) {
                        EcoWiseText(
                            textMsg = "Please select a packaging material",
                            textSize = 12.sp,
                            textColor = AlertRed,
                            textWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }
            }

            //  Organic Certification Dropdown
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    InputFieldLabel(text = "Organic Certification")
                    Box {
                        OutlinedCard(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clickable { showOrganicDropdown = true }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                EcoWiseText(
                                    textMsg = if (isOrganic) "Yes" else "No",
                                    textSize = 15.sp,
                                    textColor = TextPrimary
                                )
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.arrow_down,
                                    iconContentDescription = "Dropdown",
                                    iconSize = 16.dp,
                                    iconColor = TextSecondary
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showOrganicDropdown,
                            onDismissRequest = { showOrganicDropdown = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("No") },
                                onClick = {
                                    isOrganic = false
                                    showOrganicDropdown = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Yes") },
                                onClick = {
                                    isOrganic = true
                                    showOrganicDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            //  Eco Labels Selection Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InputFieldLabel(text = "Eco Labels")
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ecoLabelOptions.take(3).forEach { label ->
                                val isSelected = selectedEcoLabels.contains(label)
                                EcoFilterChip(
                                    label = label,
                                    isSelected = isSelected,
                                    onToggle = {
                                        selectedEcoLabels = if (isSelected) {
                                            selectedEcoLabels - label
                                        } else {
                                            selectedEcoLabels + label
                                        }
                                    }
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ecoLabelOptions.drop(3).forEach { label ->
                                val isSelected = selectedEcoLabels.contains(label)
                                EcoFilterChip(
                                    label = label,
                                    isSelected = isSelected,
                                    onToggle = {
                                        selectedEcoLabels = if (isSelected) {
                                            selectedEcoLabels - label
                                        } else {
                                            selectedEcoLabels + label
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        hasAttemptedSubmit = true
                        var isValid = true

                        if (category.isBlank()) {
                            categoryError = true
                            isValid = false
                        } else {
                            categoryError = false
                        }

                        if (productName.isBlank()) {
                            productNameError = true
                            isValid = false
                        } else {
                            productNameError = false
                        }

                        if (packagingMaterial.isBlank()) {
                            packagingMaterialError = true
                            isValid = false
                        } else {
                            packagingMaterialError = false
                        }

                        if (isValid) {
                            viewModel.analyzeAndSaveProduct(
                                name = productName.trim(),
                                brand = brandName.trim(),
                                category = category,
                                weightSize = weightSize.trim(),
                                packagingMaterial = packagingMaterial,
                                isOrganic = isOrganic,
                                ecoLabels = selectedEcoLabels.toList()
                            ) { result ->
                                navController.navigate("Product Analysis Result/${result.id}")
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green_Primary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    EcoWiseText(
                        textMsg = "View Analysis",
                        textSize = 16.sp,
                        textWeight = FontWeight.Bold,
                        textColor = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun InputFieldLabel(text: String) {
    EcoWiseText(
        textMsg = text,
        textSize = 14.sp,
        textWeight = FontWeight.SemiBold,
        textColor = TextPrimary
    )
}

@Composable
fun EcoFilterChip(
    label: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Green_Ultra_Light else Color.White,
        border = BorderStroke(
            1.dp,
            if (isSelected) Green_Forest else CardBorder
        ),
        modifier = Modifier
            .clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (isSelected) {
                EcoWiseIcon(
                    iconPainterResourceID = R.drawable.check,
                    iconContentDescription = null,
                    iconSize = 14.dp,
                    iconColor = Green_Forest
                )
            }
            EcoWiseText(
                textMsg = label,
                textSize = 13.sp,
                textWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                textColor = if (isSelected) Green_Forest else TextSecondary
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnAnalyseScreen() {
    AnalyseScreen(
        navController = rememberNavController(),
        initialCategory = "Plastic"
    )
}