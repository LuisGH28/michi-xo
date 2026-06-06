package com.luigidev.michixo.mobile.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.CatOpponent
import com.luigidev.michixo.mobile.presentation.GameUiState
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiDeepPink
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite
import com.luigidev.michixo.mobile.presentation.theme.ThemeManager
import com.luigidev.michixo.mobile.presentation.theme.ThemeType
import com.luigidev.michixo.model.Player
import com.luigidev.michixo_core.domain.SuperGatoState

@Composable
fun SuperGatoIntroScreen(
    selectedOpponent: CatOpponent,
    showFamilyGreeting: Boolean,
    onOpponentSelected: (CatOpponent) -> Unit,
    onTutorialClick: () -> Unit,
    onFamilyGreetingDismiss: () -> Unit,
    onBackClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = configuration.smallestScreenWidthDp >= 600

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MichiSoftPink)
            .navigationBarsPadding()
            .padding(
                horizontal = if (isTablet) 32.dp else 20.dp,
                vertical = if (isTablet) 24.dp else 20.dp
            )
    ) {
        SuperFloatingBackButton(
            onBackClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(10f)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SuperTopBar(
                title = stringResource(R.string.super_gato)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(if (isTablet) 28.dp else 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LuzPresentation(
                        modifier = Modifier
                            .weight(0.85f)
                            .fillMaxHeight()
                    )

                    OpponentPicker(
                        selectedOpponent = selectedOpponent,
                        onOpponentSelected = onOpponentSelected,
                        onTutorialClick = onTutorialClick,
                        modifier = Modifier.weight(1.15f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LuzPresentation(
                        modifier = Modifier
                            .fillMaxWidth(if (isTablet) 0.82f else 1f)
                            .widthIn(max = 720.dp)
                    )

                    Spacer(modifier = Modifier.height(if (isTablet) 22.dp else 14.dp))

                    OpponentPicker(
                        selectedOpponent = selectedOpponent,
                        onOpponentSelected = onOpponentSelected,
                        onTutorialClick = onTutorialClick,
                        modifier = Modifier
                            .fillMaxWidth(if (isTablet) 0.82f else 1f)
                            .widthIn(max = 720.dp)
                    )
                }
            }
        }

        if (showFamilyGreeting) {
            SuperFamilyGreetingOverlay(
                onDismiss = onFamilyGreetingDismiss,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun LuzPresentation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "luz_presenting")
    val pawWave by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(850),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pawWave"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MichiWhite,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.super_luz),
                contentDescription = stringResource(R.string.cat_luz_short),
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .graphicsLayer { rotationZ = pawWave / 2f }
                    .border(3.dp, MichiPink, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.super_intro_title),
                fontFamily = MichiFont,
                fontSize = 25.sp,
                color = MichiSoftBrown,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.super_intro_body),
                color = MichiTextPrimary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SuperFamilyGreetingOverlay(
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
            Row(
                modifier = Modifier.padding(if (isTablet) 22.dp else 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (isTablet) 18.dp else 10.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SuperGreetingImage(R.drawable.hi_lily, stringResource(R.string.opponent_lily))
                        SuperGreetingImage(R.drawable.hi_coco, stringResource(R.string.opponent_coco))
                    }
                    SuperGreetingImage(R.drawable.hi_salem, stringResource(R.string.opponent_salem))
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    ComicSpeechBubble(
                        text = stringResource(R.string.super_family_greeting),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MichiButton,
                            contentColor = MichiWhite
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
}

@Composable
private fun SuperGreetingImage(
    imageRes: Int,
    label: String
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = label,
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun OpponentPicker(
    selectedOpponent: CatOpponent,
    onOpponentSelected: (CatOpponent) -> Unit,
    onTutorialClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.choose_opponent),
            fontFamily = MichiFont,
            fontSize = 22.sp,
            color = MichiSoftBrown
        )

        CatOpponent.values().forEach { opponent ->
            OpponentCard(
                opponent = opponent,
                selected = selectedOpponent == opponent,
                onClick = { onOpponentSelected(opponent) }
            )
        }

        Button(
            onClick = onTutorialClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MichiPink,
                contentColor = MichiSoftBrown
            )
        ) {
            Icon(
                imageVector = Icons.Filled.School,
                contentDescription = stringResource(R.string.super_tutorial),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.super_tutorial),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun OpponentCard(
    opponent: CatOpponent,
    selected: Boolean,
    onClick: () -> Unit
) {
    val opponentTheme = ThemeManager.themeFor(opponent.themeType())
    val borderColor = if (selected) opponentTheme.primaryColor else opponentTheme.secondaryColor

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (selected) opponentTheme.boardColor else MichiWhite
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CatAvatar(
                name = opponent.displayName(),
                opponent = opponent,
                modifier = Modifier.size(58.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = opponent.displayName(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = opponentTheme.primaryColor
                )
                Text(
                    text = opponent.description(),
                    fontSize = 12.sp,
                    color = MichiTextPrimary
                )
            }

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = opponentTheme.primaryColor,
                    contentColor = MichiWhite
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(R.string.start_game),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SuperGatoTutorialScreen(
    onStartClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "lily_tutorial")
    val lilyOffset by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(950),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lilyOffset"
    )
    val pointerScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pointerScale"
    )

    Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MichiSoftPink)
                .navigationBarsPadding()
                .padding(20.dp)
    ) {
        SuperFloatingBackButton(
            onBackClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(10f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SuperTopBar(
                title = stringResource(R.string.super_tutorial)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 760.dp),
                shape = RoundedCornerShape(24.dp),
                color = MichiWhite,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CatAvatar(
                            name = stringResource(R.string.opponent_lily),
                            opponent = CatOpponent.LILY,
                            modifier = Modifier
                                .size(82.dp)
                                .offset(y = lilyOffset.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MichiDeepPink
                        ) {
                            Text(
                                text = stringResource(R.string.lily_tutorial_speech),
                                modifier = Modifier.padding(14.dp),
                                color = MichiSoftBrown,
                                fontSize = 14.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TutorialSuperBoard(pointerScale = pointerScale)

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = stringResource(R.string.super_tutorial_steps),
                        color = MichiTextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MichiButton,
                    contentColor = MichiWhite
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(R.string.start_game),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.start_game),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun TutorialSuperBoard(pointerScale: Float) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val boardSize = maxWidth
        SuperGatoBoard(
            uiState = GameUiState(
                selectedThemeType = ThemeType.Lily,
                superGato = SuperGatoState(
                    cells = List(81) { index ->
                        when (index) {
                            4, 12, 28 -> Player.X
                            36, 44 -> Player.O
                            else -> Player.NONE
                        }
                    },
                    activeBoard = 4
                )
            ),
            gameTheme = ThemeManager.themeFor(ThemeType.Lily),
            onCellTap = { _, _ -> },
            modifier = Modifier.fillMaxSize()
        )

        Surface(
            modifier = Modifier
                .size(boardSize * 0.12f)
                .align(Alignment.Center)
                .graphicsLayer {
                    scaleX = pointerScale
                    scaleY = pointerScale
                },
            shape = CircleShape,
            color = MichiButton.copy(alpha = 0.82f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = stringResource(R.string.cd_pets),
                    tint = MichiWhite,
                    modifier = Modifier.size(boardSize * 0.07f)
                )
            }
        }
    }
}

private fun CatOpponent.themeType(): ThemeType {
    return when (this) {
        CatOpponent.LILY -> ThemeType.Lily
        CatOpponent.COCO -> ThemeType.Coco
        CatOpponent.SALEM -> ThemeType.Salem
    }
}

@Composable
private fun SuperTopBar(
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(58.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontFamily = MichiFont,
                fontSize = 28.sp,
                color = MichiSoftBrown
            )
        }

        Spacer(modifier = Modifier.width(58.dp))
    }
}

@Composable
private fun SuperFloatingBackButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = MichiWhite,
            shadowElevation = 6.dp
        ) {}

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MichiSoftBrown,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
