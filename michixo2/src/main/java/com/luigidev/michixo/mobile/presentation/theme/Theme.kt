package com.luigidev.michixo.mobile.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MichiColorScheme = lightColorScheme(
    primary = MichiButton,
    secondary = MichiPink,
    background = MichiSoftPink,
    surface = MichiDeepPink,

    onPrimary = Color(0xFF4A4A4A),
    onSecondary = MichiSoftBrown,
    onBackground = MichiOverlay,
    onSurface = MichiTextPrimary
)

@Composable
fun MichiXOTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MichiColorScheme,
        content = content
    )
}