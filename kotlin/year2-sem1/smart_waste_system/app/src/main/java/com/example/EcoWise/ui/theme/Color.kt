package com.example.EcoWise.ui.theme

import androidx.compose.ui.graphics.Color

// 1. Primary Green Palette (Material 3 Extension)
// =========================================================================
val Green_Primary = Color(0xFF2E7D32)     // Brand & core primary color
val Green_Dark = Color(0xFF1B5E20)        // Dark mode primary
val Green_Medium = Color(0xFF388E3C)      // Medium color
val Green_Light = Color(0xFF4CAF50)       // Secondary / Active state
val Green_Forest = Color(0xFF2E7D32)      // Forest green
val Green_Bright = Color(0xFF43A047)      // Bright green
val Green_Mint = Color(0xFF1DB26E)        // Mint green ( for dark mode highlighted text )
val Green_Ultra_Light = Color(0xFF7AE7B3)

val Green_Deep_Navy = Color(0xFF111B13)   // Deep navy


// 2. Background and Surface Colors
// =========================================================================
val Green_Dark_BG = Color(0xFF1DB16D)    // Global background
val Green_Light_BG = Color(0xFFF8FAF8)    // Global background
val Green_Mint_BG = Color(0xFFD9E5DA)     // Local container / Card shimmer background
val CardBackground = Color(0xFFFFFFFF)    // Standard card background
val CardSurfaceVariant = Color(0xFFE2EFE3)// Special panel or tier base background
val CardBorder = Color(0xFFD3E4D4)        // Card border
val Divider = Color(0xFFB8D7BB)           // Divider line
val CardDarkBackground= Color(0xFFD1D5DB)

// 3. Actions and Interactive Buttons
// =========================================================================
val AgreeButton = Color(0xFF16A34A)       // Confirm / Eco-task complete button
val AgreeText = Color(0xFFFFFFFF)         // Button text color
val DisagreeButton = Color(0xFFDC2626)    // Reject / Warning button

val PrimaryActionButton = Color(0xFF2E7D32)        // Primary action button
val PrimaryActionButtonBorder = Color(0xFF2E7D32) // Primary action button border
val PrimaryActionButtonText = Color(0xFFD3FFD6)    // Button text
val PrimaryActionButtonTransparent = Color.Transparent  // Transparent button
val PrimaryIconButton = Color(0xFF949494) // Primary icon button

val SecondaryActionButton = Color(0xFFD3FFD6)        // Secondary action button
val SecondaryActionButtonText = Color(0xFF2E7D32)     // Button text

// 4. Typography and Text Levels (WCAG AA/AAA Compliant)
// =========================================================================
val DarkHeader = Color(0xFF1B5E20)        // Header dark green
val LightHeader = Color(0xFFE0F8E1)       // Header light green
val ErrorHeader = Color(0xFFC41818)       // Header red
val TextPrimary = Color(0xFF1A1C19)       // Primary text color
val TextSecondary = Color(0xFF8BA18E)     // Secondary text color
val TextMuted = Color(0xFF94A3B8)         // Muted text / Placeholder
val DeepBlack = Color(0xFF000000)         // Pure black
val DeepNavy = Color(0xFF111B13)          // Deep greenish-black
val TextTertiary = Color(0xFF1F2937)     // Tertiary text color

// 5. Tag Colors
// Eco points / Limited item tag (Amber / Gold)
val AmberBg = Color(0xFFFEF3C7)
val AmberText = Color(0xFFB45309)
val StarYellow = Color(0xFFF59E0B)
val NoticeYellow = Color(0xFFF5E90B)

// Waste sorting: Plastics (Blue)
val PlasticBg = Color(0xFFE1F5FE)
val PlasticText = Color(0xFF0288D1)

// Waste sorting: Paper (Orange)
val PaperBg = Color(0xFFFFE0B2)
val PaperText = Color(0xFFE65100)

// Waste sorting: Metal
// Blue-Gray -> color-blind friendly
val MetalBg = Color(0xFFECEFF1)
val MetalText = Color(0xFF455A64)

// Waste sorting: E-Waste (Purple)
val PurpleBg = Color(0xFFF3E8FF)
val PurpleText = Color(0xFF7E22CE)

// Waste sorting
// Error alert: Hazardous waste / AlertDialog
val AlertRed = Color(0xFFDC2626)
val AlertRedLight = Color(0xFFFEF2F2)
val AlertRedText = Color(0xFF991B1B)

// 6. EcoWiseTextField Semantic Colors (Light & Dark Support)
// =========================================================================

// --- Light Mode TextField Colors ---
val TextField_FocusedText_Light = Color(0xFF1E293B)
val TextField_UnfocusedText_Light = Color(0xFF475569)
val TextField_DisabledText_Light = Color(0xFF94A3B8)
val TextField_ErrorText_Light = Color(0xFF991B1B)

val TextField_FocusedContainer_Light = Color(0xFFE7FCED)
val TextField_UnfocusedContainer_Light = Color(0xFFDBE1DB)
val TextField_DisabledContainer_Light = Color(0xFFF1F5F9)
val TextField_ErrorContainer_Light = Color(0xFFFEF2F2)

val TextField_FocusedBorder_Light = Color(0xFF16A34A)
val TextField_UnfocusedBorder_Light = Color(0xFFCBD5E1)
val TextField_DisabledBorder_Light = Color(0xFFE2E8F0)
val TextField_ErrorBorder_Light = Color(0xFFDC2626)

val TextField_Cursor_Light = Color(0xFF16A34A)
val TextField_ErrorCursor_Light = Color(0xFFDC2626)

val TextField_SelectionHandle_Light = Color(0xFF16A34A)
val TextField_SelectionBg_Light = Color(0xFFBBF7D0)

val TextField_FocusedIcon_Light = Color(0xFF16A34A)
val TextField_UnfocusedIcon_Light = Color(0xFF64748B)
val TextField_DisabledIcon_Light = Color(0xFFCBD5E1)
val TextField_ErrorIcon_Light = Color(0xFFDC2626)

val TextField_FocusedLabel_Light = Color(0xFF16A34A)
val TextField_UnfocusedLabel_Light = Color(0xFF64748B)
val TextField_DisabledLabel_Light = Color(0xFF94A3B8)
val TextField_ErrorLabel_Light = Color(0xFFDC2626)

val TextField_FocusedPlaceholder_Light = Color(0xFF86EFAC)
val TextField_UnfocusedPlaceholder_Light = Color(0xFF94A3B8)
val TextField_DisabledPlaceholder_Light = Color(0xFFCBD5E1)
val TextField_ErrorPlaceholder_Light = Color(0xFFFCA5A5)

val TextField_FocusedSupportingText_Light = Color(0xFF16A34A)
val TextField_UnfocusedSupportingText_Light = Color(0xFF64748B)
val TextField_DisabledSupportingText_Light = Color(0xFF94A3B8)
val TextField_ErrorSupportingText_Light = Color(0xFFDC2626)

val TextField_FocusedPrefixSuffix_Light = Color(0xFF15803D)
val TextField_UnfocusedPrefixSuffix_Light = Color(0xFF475569)
val TextField_DisabledPrefixSuffix_Light = Color(0xFF94A3B8)
val TextField_ErrorPrefixSuffix_Light = Color(0xFFB91C1C)


// --- Dark Mode TextField Colors ---
val TextField_FocusedText_Dark = Color(0xFFF1F5F9)
val TextField_UnfocusedText_Dark = Color(0xFF94A3B8)
val TextField_DisabledText_Dark = Color(0xFF64748B)
val TextField_ErrorText_Dark = Color(0xFFFCA5A5)

val TextField_FocusedContainer_Dark = Color(0xFF064E3B)
val TextField_UnfocusedContainer_Dark = Color(0xFF1E293B)
val TextField_DisabledContainer_Dark = Color(0xFF0F172A)
val TextField_ErrorContainer_Dark = Color(0xFF451A03)

val TextField_FocusedBorder_Dark = Color(0xFF4ADE80)
val TextField_UnfocusedBorder_Dark = Color(0xFF475569)
val TextField_DisabledBorder_Dark = Color(0xFF334155)
val TextField_ErrorBorder_Dark = Color(0xFFEF4444)

val TextField_Cursor_Dark = Color(0xFF4ADE80)
val TextField_ErrorCursor_Dark = Color(0xFFEF4444)

val TextField_SelectionHandle_Dark = Color(0xFF4ADE80)
val TextField_SelectionBg_Dark = Color(0xFF065F46)

val TextField_FocusedIcon_Dark = Color(0xFF4ADE80)
val TextField_UnfocusedIcon_Dark = Color(0xFF94A3B8)
val TextField_DisabledIcon_Dark = Color(0xFF475569)
val TextField_ErrorIcon_Dark = Color(0xFFEF4444)

val TextField_FocusedLabel_Dark = Color(0xFF4ADE80)
val TextField_UnfocusedLabel_Dark = Color(0xFF94A3B8)
val TextField_DisabledLabel_Dark = Color(0xFF64748B)
val TextField_ErrorLabel_Dark = Color(0xFFEF4444)

val TextField_FocusedPlaceholder_Dark = Color(0xFF86EFAC)
val TextField_UnfocusedPlaceholder_Dark = Color(0xFF64748B)
val TextField_DisabledPlaceholder_Dark = Color(0xFF475569)
val TextField_ErrorPlaceholder_Dark = Color(0xFFF87171)

val TextField_FocusedSupportingText_Dark = Color(0xFF4ADE80)
val TextField_UnfocusedSupportingText_Dark = Color(0xFF94A3B8)
val TextField_DisabledSupportingText_Dark = Color(0xFF64748B)
val TextField_ErrorSupportingText_Dark = Color(0xFFEF4444)

val TextField_FocusedPrefixSuffix_Dark = Color(0xFF4ADE80)
val TextField_UnfocusedPrefixSuffix_Dark = Color(0xFF94A3B8)
val TextField_DisabledPrefixSuffix_Dark = Color(0xFF64748B)
val TextField_ErrorPrefixSuffix_Dark = Color(0xFFF87171)