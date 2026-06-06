package com.luigidev.michixo.mobile.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
private fun michiColorScheme(): ColorScheme = lightColorScheme(
    primary = MichiButton,
    secondary = MichiPink,
    background = MichiSoftPink,
    surface = MichiDeepPink,

    onPrimary = MichiTextPrimary,
    onSecondary = MichiSoftBrown,
    onBackground = MichiOverlay,
    onSurface = MichiTextPrimary
)

@Composable
fun MichiXOTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = michiColorScheme(),
        content = content
    )
}
