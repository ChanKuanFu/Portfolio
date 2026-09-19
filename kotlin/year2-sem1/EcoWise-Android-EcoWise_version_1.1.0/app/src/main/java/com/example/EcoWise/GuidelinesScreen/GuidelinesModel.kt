package com.example.EcoWise.GuidelinesScreen


data class GuidelinePage(
    val title: String,
    val subtitle: String,
    val showIcon: String
)

val guidelinePages = listOf(
    GuidelinePage(
        title = "EcoWise",
        subtitle = "Responsible Consumption for a Greener Planet",
        showIcon = "leaf"
    ),
    GuidelinePage(
        title = "Know Your Impact",
        subtitle = "Analyse to discover the environmental footprint of your products",
        showIcon = "phone_scan"
    ),
    GuidelinePage(
        title = "Make Better Choices",
        subtitle = "Compare products and find greener alternatives to reduce your personal carbon footprint.",
        showIcon = "badge"
    ),
    GuidelinePage(
        title = "Earn & Grow",
        subtitle = "Complete daily eco challenges to earn points and rewards.",
        showIcon = "trophy"
    )
)