package com.luigidev.michixo.mobile.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R

val MichiFont = FontFamily(
    Font(R.font.gochi_hand)
)

val MichiTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = MichiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)