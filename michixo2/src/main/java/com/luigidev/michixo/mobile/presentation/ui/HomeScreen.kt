package com.luigidev.michixo.mobile.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiDeepPink
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme

@Composable
fun HomeScreen(vm: GameViewModel) {
    var showDifficultyDialog by remember { mutableStateOf(false) }
    val uiState by vm.uiState.collectAsState()

    HomeScreenContent(
        onPlayClick = { showDifficultyDialog = true },
        onSuperGatoClick = { vm.showSuperGatoIntro() },
        onSettingsClick = { vm.goToSettings() },
        showFamilyGreeting = uiState.showHomeFamilyGreeting,
        onFamilyGreetingDismiss = { vm.dismissHomeFamilyGreeting() }
    )

    if (showDifficultyDialog) {
        DifficultyDialog(
            selectedDifficulty = uiState.difficulty,
            onDismiss = { showDifficultyDialog = false },
            onConfirm = { difficulty ->
                vm.setDifficulty(difficulty)
                showDifficultyDialog = false
                vm.startGame()
            }
        )
    }
}

@Composable
fun HomeScreenContent(
    onPlayClick: () -> Unit,
    onSuperGatoClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    showFamilyGreeting: Boolean = false,
    onFamilyGreetingDismiss: () -> Unit = {}
) {
    var startAnimation by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = configuration.smallestScreenWidthDp >= 600

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val headerAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "headerAlpha"
    )

    val headerOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -30f,
        animationSpec = tween(durationMillis = 500),
        label = "headerOffsetY"
    )

    val imageAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 700, delayMillis = 120),
        label = "imageAlpha"
    )

    val imageOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 80f,
        animationSpec = tween(durationMillis = 700, delayMillis = 120),
        label = "imageOffsetY"
    )

    val titleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 550, delayMillis = 260),
        label = "titleAlpha"
    )

    val titleOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 30f,
        animationSpec = tween(durationMillis = 550, delayMillis = 260),
        label = "titleOffsetY"
    )

    val subtitleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 550, delayMillis = 340),
        label = "subtitleAlpha"
    )

    val subtitleOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 24f,
        animationSpec = tween(durationMillis = 550, delayMillis = 340),
        label = "subtitleOffsetY"
    )

    val playButtonAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 430),
        label = "playButtonAlpha"
    )

    val playButtonOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = tween(durationMillis = 600, delayMillis = 430),
        label = "playButtonOffsetY"
    )

    val playButtonScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.88f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "playButtonScale"
    )

    val settingsAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 520),
        label = "settingsAlpha"
    )

    val settingsOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = tween(durationMillis = 600, delayMillis = 520),
        label = "settingsOffsetY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MichiSoftPink)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeHeader(
                headerAlpha = headerAlpha,
                headerOffsetY = headerOffsetY
            )

            if (isTablet && isLandscape) {
                TabletLandscapeHomeContent(
                    imageAlpha = imageAlpha,
                    imageOffsetY = imageOffsetY,
                    titleAlpha = titleAlpha,
                    titleOffsetY = titleOffsetY,
                    subtitleAlpha = subtitleAlpha,
                    subtitleOffsetY = subtitleOffsetY,
                    playButtonAlpha = playButtonAlpha,
                    playButtonOffsetY = playButtonOffsetY,
                    playButtonScale = playButtonScale,
                    settingsAlpha = settingsAlpha,
                    settingsOffsetY = settingsOffsetY,
                    onPlayClick = onPlayClick,
                    onSuperGatoClick = onSuperGatoClick,
                    onSettingsClick = onSettingsClick
                )
            } else {
                PortraitHomeContent(
                    imageAlpha = imageAlpha,
                    imageOffsetY = imageOffsetY,
                    titleAlpha = titleAlpha,
                    titleOffsetY = titleOffsetY,
                    subtitleAlpha = subtitleAlpha,
                    subtitleOffsetY = subtitleOffsetY,
                    playButtonAlpha = playButtonAlpha,
                    playButtonOffsetY = playButtonOffsetY,
                    playButtonScale = playButtonScale,
                    settingsAlpha = settingsAlpha,
                    settingsOffsetY = settingsOffsetY,
                    onPlayClick = onPlayClick,
                    onSuperGatoClick = onSuperGatoClick,
                    onSettingsClick = onSettingsClick
                )
            }
        }

        if (showFamilyGreeting) {
            HomeFamilyGreetingOverlay(
                onDismiss = onFamilyGreetingDismiss,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun HomeFamilyGreetingOverlay(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600

    Box(
        modifier = modifier
            .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.18f))
            .padding(if (isTablet) 28.dp else 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(if (isTablet) 0.82f else 1f)
                .widthIn(max = 840.dp),
            shape = RoundedCornerShape(26.dp),
            color = MichiSoftPink,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(if (isTablet) 22.dp else 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(if (isTablet) 20.dp else 12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.super_luz),
                        contentDescription = stringResource(R.string.cat_luz_short),
                        modifier = Modifier
                            .size(if (isTablet) 180.dp else 118.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )

                    ComicSpeechBubble(
                        text = stringResource(R.string.home_family_greeting),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GreetingMiniImage(
                        imageRes = R.drawable.hi_lily,
                        label = stringResource(R.string.opponent_lily)
                    )
                    GreetingMiniImage(
                        imageRes = R.drawable.hi_coco,
                        label = stringResource(R.string.opponent_coco)
                    )
                    GreetingMiniImage(
                        imageRes = R.drawable.hi_salem,
                        label = stringResource(R.string.opponent_salem)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MichiButton,
                        contentColor = MichiSoftPink
                    )
                ) {
                    Text(
                        text = stringResource(R.string.super_greeting_continue),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun GreetingMiniImage(
    imageRes: Int,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = label,
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = MichiSoftBrown,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

@Composable
fun HomeHeader(
    headerAlpha: Float,
    headerOffsetY: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .graphicsLayer {
                alpha = headerAlpha
                translationY = headerOffsetY
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(26.dp),
            shape = CircleShape,
            color = MichiPink
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = stringResource(R.string.michi_icon),
                    modifier = Modifier.size(14.dp),
                    tint = MichiSoftBrown
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.app_name),
            fontFamily = MichiFont,
            fontSize = 16.sp,
            color = MichiSoftBrown
        )
    }
}

@Composable
fun PortraitHomeContent(
    imageAlpha: Float,
    imageOffsetY: Float,
    titleAlpha: Float,
    titleOffsetY: Float,
    subtitleAlpha: Float,
    subtitleOffsetY: Float,
    playButtonAlpha: Float,
    playButtonOffsetY: Float,
    playButtonScale: Float,
    settingsAlpha: Float,
    settingsOffsetY: Float,
    onPlayClick: () -> Unit,
    onSuperGatoClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HomeHeroImage(
            modifier = Modifier
                .size(width = 210.dp, height = 250.dp)
                .graphicsLayer {
                    alpha = imageAlpha
                    translationY = imageOffsetY
                }
        )

        Spacer(modifier = Modifier.height(18.dp))

        HomeTitle(
            titleAlpha = titleAlpha,
            titleOffsetY = titleOffsetY,
            fontSize = 40
        )

        Spacer(modifier = Modifier.height(6.dp))

        HomeSubtitle(
            subtitleAlpha = subtitleAlpha,
            subtitleOffsetY = subtitleOffsetY
        )

        Spacer(modifier = Modifier.height(24.dp))

        HomeButtons(
            playButtonAlpha = playButtonAlpha,
            playButtonOffsetY = playButtonOffsetY,
            playButtonScale = playButtonScale,
            settingsAlpha = settingsAlpha,
            settingsOffsetY = settingsOffsetY,
            buttonWidthFraction = 1f,
            onPlayClick = onPlayClick,
            onSuperGatoClick = onSuperGatoClick,
            onSettingsClick = onSettingsClick
        )
    }
}

@Composable
fun TabletLandscapeHomeContent(
    imageAlpha: Float,
    imageOffsetY: Float,
    titleAlpha: Float,
    titleOffsetY: Float,
    subtitleAlpha: Float,
    subtitleOffsetY: Float,
    playButtonAlpha: Float,
    playButtonOffsetY: Float,
    playButtonScale: Float,
    settingsAlpha: Float,
    settingsOffsetY: Float,
    onPlayClick: () -> Unit,
    onSuperGatoClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HomeHeroImage(
                modifier = Modifier
                    .size(width = 260.dp, height = 300.dp)
                    .graphicsLayer {
                        alpha = imageAlpha
                        translationY = imageOffsetY
                    }
            )

            Spacer(modifier = Modifier.height(18.dp))

            HomeTitle(
                titleAlpha = titleAlpha,
                titleOffsetY = titleOffsetY,
                fontSize = 42
            )

            Spacer(modifier = Modifier.height(6.dp))

            HomeSubtitle(
                subtitleAlpha = subtitleAlpha,
                subtitleOffsetY = subtitleOffsetY
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HomeButtons(
                playButtonAlpha = playButtonAlpha,
                playButtonOffsetY = playButtonOffsetY,
                playButtonScale = playButtonScale,
                settingsAlpha = settingsAlpha,
                settingsOffsetY = settingsOffsetY,
                buttonWidthFraction = 0.85f,
                onPlayClick = onPlayClick,
                onSuperGatoClick = onSuperGatoClick,
                onSettingsClick = onSettingsClick
            )
        }
    }
}

@Composable
fun HomeHeroImage(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(25.dp),
        color = MichiDeepPink
    ) {
        Box(
            modifier = Modifier.padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.super_brothers),
                contentDescription = stringResource(R.string.luz_image),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(22.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun HomeTitle(
    titleAlpha: Float,
    titleOffsetY: Float,
    fontSize: Int
) {
    Text(
        text = stringResource(R.string.app_name),
        fontFamily = MichiFont,
        fontSize = fontSize.sp,
        color = MichiSoftBrown,
        modifier = Modifier.graphicsLayer {
            alpha = titleAlpha
            translationY = titleOffsetY
        }
    )
}

@Composable
fun HomeSubtitle(
    subtitleAlpha: Float,
    subtitleOffsetY: Float
) {
    Text(
        text = stringResource(R.string.lest_play),
        fontSize = 13.sp,
        color = MichiTextPrimary,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.graphicsLayer {
            alpha = subtitleAlpha
            translationY = subtitleOffsetY
        }
    )
}

@Composable
fun HomeButtons(
    playButtonAlpha: Float,
    playButtonOffsetY: Float,
    playButtonScale: Float,
    settingsAlpha: Float,
    settingsOffsetY: Float,
    buttonWidthFraction: Float,
    onPlayClick: () -> Unit,
    onSuperGatoClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Button(
        onClick = onPlayClick,
        modifier = Modifier
            .fillMaxWidth(buttonWidthFraction)
            .height(56.dp)
            .graphicsLayer {
                alpha = playButtonAlpha
                translationY = playButtonOffsetY
                scaleX = playButtonScale
                scaleY = playButtonScale
            },
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MichiButton,
            contentColor = MichiTextPrimary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = stringResource(R.string.play),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.play),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    SecondaryButton(
        modifier = Modifier
            .fillMaxWidth(buttonWidthFraction)
            .graphicsLayer {
                alpha = settingsAlpha
                translationY = settingsOffsetY
            },
        text = stringResource(R.string.super_gato),
        icon = Icons.Filled.Pets,
        onClick = onSuperGatoClick
    )

    Spacer(modifier = Modifier.height(12.dp))

    SecondaryButton(
        modifier = Modifier
            .fillMaxWidth(buttonWidthFraction)
            .graphicsLayer {
                alpha = settingsAlpha
                translationY = settingsOffsetY
            },
        text = stringResource(R.string.settings_title),
        icon = Icons.Filled.Settings,
        onClick = onSettingsClick
    )
}

@Composable
private fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MichiPink,
            contentColor = MichiSoftBrown
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MichiXOTheme {
        HomeScreenContent(
            onPlayClick = {},
            onSuperGatoClick = {},
            onSettingsClick = {}
        )
    }
}
