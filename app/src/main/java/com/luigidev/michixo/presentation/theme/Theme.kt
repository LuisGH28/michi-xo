package com.luigidev.michixo.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

private val MichiWearColors = Colors(
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
        colors = MichiWearColors,
        content = content
    )
}