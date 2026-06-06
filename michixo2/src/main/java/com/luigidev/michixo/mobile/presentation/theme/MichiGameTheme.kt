package com.luigidev.michixo.mobile.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.annotation.DrawableRes

data class MichiGameTheme(
    val themeType: ThemeType,
    val themeName: String,
    val backgroundColor: Color,
    val boardColor: Color,
    val primaryColor: Color,
    val secondaryColor: Color,
    @DrawableRes val xIcon: Int,
    @DrawableRes val oIcon: Int,
    val xColor: Color,
    val oColor: Color,
    val victoryColor: Color,
    val pauseCardColor: Color,
    val pauseTextColor: Color,
    val pausePrimaryButtonColor: Color,
    val pauseSecondaryButtonColor: Color,
    val pauseAccentColor: Color,
    val useLibraryPawForO: Boolean = false
)
