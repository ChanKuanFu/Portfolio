package com.example.EcoWise.GuidelinesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseButton
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R
import kotlinx.coroutines.launch


@Composable
fun GuidelinesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Show Page-by -page as an introduction
    val guidelinesPagerState = rememberPagerState(pageCount = { guidelinePages.size })
    // remember pager state
    val coroutineScope = rememberCoroutineScope()

    // Text
    val skipFont = MaterialTheme.typography.labelSmall
    val titleFont = MaterialTheme.typography.titleLarge
    val contentFont = MaterialTheme.typography.bodyMedium

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .background(Green_Mint_BG)
            .statusBarsPadding() // avoid overlapping with top bar
            .navigationBarsPadding() // avoid overlapping with bottom bar
            .padding(25.dp)
    ){
        // upper part section -- Skip textbutton
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ){
            // TextButton
            EcoWiseButton(
                buttonEnabled = true,
                buttonColor = PrimaryActionButtonTransparent,
                text = "Skip",
                textSize = 15.sp,
                textColor = PrimaryActionButton,
                textWeight = skipFont.fontWeight,
                onClickChange = {
                    navController.navigate("EcoWise Login")
                }
            )
        }

        // center part section -- 4 pages for introduction
        HorizontalPager(
            state = guidelinesPagerState,  // current page state
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndexOf ->
            val page = guidelinePages[pageIndexOf]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(210.dp)
                        .fillMaxWidth()
                ) {
                    when (page.showIcon) {
                        "leaf" -> {
                            Surface(
                                shape = RoundedCornerShape(28.dp),
                                color = Color.White,
                                shadowElevation = 4.dp,
                                modifier = Modifier.size(115.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = R.drawable.eco,
                                        iconContentDescription =  "Badge",
                                        iconColor =  PrimaryActionButton,
                                        iconSize = 56.dp
                                    )
                                }
                            }
                        }
                        "phone_scan" -> {
                            Surface(
                                shape = RoundedCornerShape(28.dp),
                                color = Color.White,
                                shadowElevation = 4.dp,
                                modifier = Modifier.size(115.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = R.drawable.document_search,
                                        iconContentDescription =  "Badge",
                                        iconColor =  PrimaryActionButton,
                                        iconSize = 56.dp
                                    )
                                }
                            }
                        }
                        "badge" -> {
                            Surface(
                                shape = RoundedCornerShape(28.dp),
                                color = Color.White,
                                shadowElevation = 4.dp,
                                modifier = Modifier.size(115.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = R.drawable.verified,
                                        iconContentDescription =  "Badge",
                                        iconColor =  PrimaryActionButton,
                                        iconSize = 56.dp
                                    )
                                }
                            }
                        }
                        "trophy" -> {
                            Surface(
                                shape = RoundedCornerShape(28.dp),
                                color = Color.White,
                                shadowElevation = 4.dp,
                                modifier = Modifier.size(115.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = R.drawable.outlined_challenge,
                                        iconContentDescription =  "Badge",
                                        iconColor =  PrimaryActionButton,
                                        iconSize = 56.dp
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                // Title
                EcoWiseText(
                    textMsg = page.title,
                    textSize = titleFont.fontSize,
                    textWeight = FontWeight.ExtraBold,
                    textColor = Green_Primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))
                EcoWiseText(
                    textMsg = page.subtitle,
                    textSize = 15.sp,
                    textAlign = TextAlign.Center,
                    textColor = Green_Bright
                )
            }
        }

        // Botton Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ){
            // 4 page indicator dots in row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                // show page dots
                repeat(guidelinePages.size) { pageIndex ->
                    val isActiveCurrentPage = guidelinesPagerState.currentPage == pageIndex
                    Box(modifier = Modifier
                        .height(7.dp)
                        .width(if (isActiveCurrentPage) 25.dp else 7.dp)
                        .clip(CircleShape)
                        .background(if (isActiveCurrentPage) Green_Forest else CardDarkBackground)
                        .clickable {
                            /*
                                remember pager state
                                val coroutineScope = rememberCoroutineScope()
                            */
                            coroutineScope.launch {
                                // animation when scroll to next or previous intro page
                                guidelinesPagerState.animateScrollToPage(pageIndex)
                            }
                        }

                    )
                }
            }

            // Next and Get Started Button
            val showMsg : String =
                if(guidelinesPagerState.currentPage == (guidelinePages.size-1)) "Get Started"
                else "Next"

            EcoWiseButton(
                buttonEnabled = true,
                buttonColor = PrimaryActionButton,
                buttonSize = 70.dp,
                buttonRoundedCornerShapeTopStart = 50,
                buttonRoundedCornerShapeBottomEnd = 50,
                buttonRoundedCornerShapeTopEnd = 15,
                buttonRoundedCornerShapeBottomStart = 15,
                buttonElevation = 2.dp,
                buttonBorderWidth = 2.dp,
                buttonBorderColor = PrimaryActionButtonBorder,
                buttonContentPadding = 5.dp,
                buttonIsFillMaxWidth = true,

                text = showMsg,
                textSize = 15.sp,
                textWeight = FontWeight.SemiBold,
                textFamily = contentFont.fontFamily,
                textColor = PrimaryActionButtonText,
                textPadding = 5.dp,

                iconPainterResourceID = R.drawable.arrowforward,
                iconContentDescription = "Next step",
                iconSize = 20.dp,
                iconColor = SecondaryActionButton,
                iconPosition = "End",

                onClickChange = {
                    if (guidelinesPagerState.currentPage < (guidelinePages.size-1)) {
                        coroutineScope.launch {
                            guidelinesPagerState.animateScrollToPage(guidelinesPagerState.currentPage + 1)
                        }
                    } else {
                        navController.navigate("EcoWise Login")
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnGuidelinesScreen(){
    GuidelinesScreen(
        navController = rememberNavController()
    )
}
