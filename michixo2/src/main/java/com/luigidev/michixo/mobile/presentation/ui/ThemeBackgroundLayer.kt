package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.luigidev.michixo.mobile.presentation.theme.MichiGameTheme
import com.luigidev.michixo.mobile.presentation.theme.ThemeType
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ThemeBackgroundLayer(
    theme: MichiGameTheme,
    modifier: Modifier = Modifier
) {
    if (theme.themeType == ThemeType.Luz) return

    val transition = rememberInfiniteTransition(label = "theme_background_${theme.themeType.id}")
    val slowProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "slow_theme_motion"
    )
    val loopProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing)
        ),
        label = "loop_theme_motion"
    )
    val twinkleProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing)
        ),
        label = "twinkle_theme_motion"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        when (theme.themeType) {
            ThemeType.Coco -> drawCocoSky(theme, slowProgress)
            ThemeType.Lily -> drawLilyGarden(theme, slowProgress, loopProgress)
            ThemeType.Salem -> drawSalemGalaxy(theme, twinkleProgress, loopProgress)
            ThemeType.Luz -> Unit
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCocoSky(
    theme: MichiGameTheme,
    progress: Float
) {
    val arc = PI + (PI * progress)
    val sunX = size.width * (0.50f + 0.34f * cos(arc).toFloat())
    val sunY = size.height * (0.18f - 0.07f * sin(arc).toFloat())
    drawCircle(
        color = theme.pauseSecondaryButtonColor.copy(alpha = 0.46f),
        radius = size.minDimension * 0.12f,
        center = Offset(sunX, sunY)
    )
    val cloudDrift = (progress - 0.5f) * size.width * 0.06f
    drawCloud(
        color = theme.pauseCardColor.copy(alpha = 0.82f),
        center = Offset(size.width * 0.18f + cloudDrift, size.height * 0.18f),
        scale = size.minDimension * 0.18f
    )
    drawCloud(
        color = theme.pauseCardColor.copy(alpha = 0.66f),
        center = Offset(size.width * 0.82f - cloudDrift, size.height * 0.78f),
        scale = size.minDimension * 0.16f
    )
    listOf(
        Offset(size.width * 0.25f, size.height * 0.78f),
        Offset(size.width * 0.72f, size.height * 0.28f),
        Offset(size.width * 0.58f, size.height * 0.08f)
    ).forEach { point ->
        drawCircle(theme.primaryColor.copy(alpha = 0.28f), radius = 3.5.dp.toPx(), center = point)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLilyGarden(
    theme: MichiGameTheme,
    swayProgress: Float,
    butterflyProgress: Float
) {
    val sway = sin(swayProgress * PI * 2).toFloat()
    drawCircle(
        color = theme.pauseAccentColor.copy(alpha = 0.16f),
        radius = size.minDimension * 0.28f,
        center = Offset(size.width * 0.14f, size.height * 0.14f)
    )
    drawCircle(
        color = theme.pausePrimaryButtonColor.copy(alpha = 0.13f),
        radius = size.minDimension * 0.22f,
        center = Offset(size.width * 0.88f, size.height * 0.86f)
    )
    listOf(
        Offset(size.width * 0.16f, size.height * 0.78f),
        Offset(size.width * 0.30f, size.height * 0.88f),
        Offset(size.width * 0.74f, size.height * 0.86f),
        Offset(size.width * 0.86f, size.height * 0.68f)
    ).forEachIndexed { index, point ->
        drawTinyFlower(
            theme = theme,
            center = point.copy(x = point.x + sway * (index + 1) * 1.2.dp.toPx()),
            radius = size.minDimension * 0.024f
        )
    }
    val butterflyAlpha = edgeFade(butterflyProgress)
    val butterflyX = size.width * (-0.10f + butterflyProgress * 1.20f)
    val butterflyY = size.height * (0.76f - 0.34f * sin(butterflyProgress * PI).toFloat())
    drawButterfly(theme, Offset(butterflyX, butterflyY), butterflyAlpha)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSalemGalaxy(
    theme: MichiGameTheme,
    twinkleProgress: Float,
    shootingProgress: Float
) {
    drawCircle(
        color = theme.pausePrimaryButtonColor.copy(alpha = 0.10f),
        radius = size.minDimension * 0.36f,
        center = Offset(size.width * 0.88f, size.height * 0.18f)
    )
    drawCircle(
        color = theme.pauseAccentColor.copy(alpha = 0.07f),
        radius = size.minDimension * 0.28f,
        center = Offset(size.width * 0.16f, size.height * 0.84f)
    )
    listOf(
        Offset(size.width * 0.18f, size.height * 0.18f),
        Offset(size.width * 0.30f, size.height * 0.62f),
        Offset(size.width * 0.74f, size.height * 0.22f),
        Offset(size.width * 0.86f, size.height * 0.70f),
        Offset(size.width * 0.54f, size.height * 0.86f)
    ).forEachIndexed { index, point ->
        val alphaWave = 0.30f + 0.32f * ((sin((twinkleProgress + index * 0.17f) * PI * 2) + 1f) / 2f).toFloat()
        drawCircle(
            color = theme.pauseAccentColor.copy(alpha = alphaWave),
            radius = if (index % 2 == 0) 3.dp.toPx() else 2.dp.toPx(),
            center = point
        )
    }
    val starAlpha = edgeFade(shootingProgress).coerceAtMost(if (shootingProgress < 0.58f) 1f else 0f)
    val starCenter = Offset(
        x = size.width * (0.96f - shootingProgress * 1.05f),
        y = size.height * (0.10f + shootingProgress * 0.55f)
    )
    rotate(degrees = -28f, pivot = starCenter) {
        drawLine(
            color = theme.pausePrimaryButtonColor.copy(alpha = 0.44f * starAlpha),
            start = starCenter.copy(x = starCenter.x - size.minDimension * 0.18f),
            end = starCenter,
            strokeWidth = 3.dp.toPx()
        )
        drawCircle(
            color = theme.pausePrimaryButtonColor.copy(alpha = 0.78f * starAlpha),
            radius = 5.dp.toPx(),
            center = starCenter
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCloud(
    color: androidx.compose.ui.graphics.Color,
    center: Offset,
    scale: Float
) {
    drawCircle(color, radius = scale * 0.28f, center = center.copy(x = center.x - scale * 0.28f))
    drawCircle(color, radius = scale * 0.36f, center = center.copy(y = center.y - scale * 0.12f))
    drawCircle(color, radius = scale * 0.26f, center = center.copy(x = center.x + scale * 0.30f))
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - scale * 0.56f, center.y),
        size = Size(scale * 1.12f, scale * 0.34f),
        cornerRadius = CornerRadius(scale * 0.16f, scale * 0.16f)
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTinyFlower(
    theme: MichiGameTheme,
    center: Offset,
    radius: Float
) {
    drawCircle(theme.pausePrimaryButtonColor.copy(alpha = 0.34f), radius, center.copy(y = center.y - radius))
    drawCircle(theme.pausePrimaryButtonColor.copy(alpha = 0.34f), radius, center.copy(x = center.x + radius))
    drawCircle(theme.pausePrimaryButtonColor.copy(alpha = 0.34f), radius, center.copy(y = center.y + radius))
    drawCircle(theme.pausePrimaryButtonColor.copy(alpha = 0.34f), radius, center.copy(x = center.x - radius))
    drawCircle(theme.pauseAccentColor.copy(alpha = 0.52f), radius * 0.62f, center)
    drawCircle(theme.pauseAccentColor.copy(alpha = 0.34f), radius * 1.7f, center, style = Stroke(width = 1.5.dp.toPx()))
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawButterfly(
    theme: MichiGameTheme,
    center: Offset,
    alpha: Float
) {
    val wing = size.minDimension * 0.018f
    drawCircle(theme.pausePrimaryButtonColor.copy(alpha = 0.34f * alpha), wing, center.copy(x = center.x - wing))
    drawCircle(theme.pausePrimaryButtonColor.copy(alpha = 0.34f * alpha), wing, center.copy(x = center.x + wing))
    drawCircle(theme.pauseAccentColor.copy(alpha = 0.54f * alpha), wing * 0.42f, center)
    drawLine(
        color = theme.pauseAccentColor.copy(alpha = 0.48f * alpha),
        start = center.copy(y = center.y - wing * 1.2f),
        end = center.copy(y = center.y + wing * 1.2f),
        strokeWidth = 1.5.dp.toPx()
    )
}

private fun edgeFade(progress: Float): Float {
    return when {
        progress < 0.12f -> progress / 0.12f
        progress > 0.58f -> ((1f - progress) / 0.42f).coerceIn(0f, 1f)
        else -> 1f
    }
}
