package com.luigidev.michixo.mobile.presentation.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.GameMode
import com.luigidev.michixo.mobile.presentation.GameUiState
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.Screen
import com.luigidev.michixo.mobile.presentation.theme.MichiBoard
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiGameTheme
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme
import com.luigidev.michixo.mobile.presentation.theme.ThemeManager
import com.luigidev.michixo.mobile.presentation.theme.ThemeType
import com.luigidev.michixo.mobile.presentation.util.VibrationHelper
import com.luigidev.michixo.model.Player

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TicTacToeScreen(
    vm: GameViewModel,
    onExitApp: () -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onSuperMichiIntroDismissed: () -> Unit = {}
){
    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current

    var showPauseDialog by remember { mutableStateOf(false) }
    var showPauseSettingsDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.winner, uiState.isDraw, uiState.screen) {
        if (uiState.screen == Screen.RESULT && uiState.vibrationEnabled) {
            when {
                uiState.winner == Player.X -> {
                    VibrationHelper.vibrate(context, 90)
                }

                uiState.winner == Player.O -> {
                    VibrationHelper.vibrate(context, 180)
                }

                uiState.isDraw -> {
                    VibrationHelper.vibrate(context, 70)
                }
            }
        }
    }

    BackHandler {
        when {
            showInfoDialog -> {
                showInfoDialog = false
            }

            showPauseSettingsDialog -> {
                showPauseSettingsDialog = false
            }

            showPauseDialog -> {
                showPauseDialog = false
            }

            uiState.screen == Screen.HOME -> {
                onExitApp()
            }

            uiState.screen == Screen.SETTINGS -> {
                vm.backToHome()
            }

            uiState.screen == Screen.RESULT -> {
                vm.backToHome()
            }

            uiState.screen == Screen.SUPER_INTRO || uiState.screen == Screen.SUPER_TUTORIAL -> {
                vm.backToHome()
            }

            uiState.screen == Screen.GAME -> {
                showPauseDialog = true
            }
        }
    }

    AnimatedContent(
        targetState = uiState.screen,
        transitionSpec = {
            screenTransition(targetState)
        },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            Screen.HOME -> HomeScreen(vm)

            Screen.SUPER_INTRO -> SuperGatoIntroScreen(
                selectedOpponent = uiState.opponent,
                showFamilyGreeting = uiState.showSuperFamilyGreeting,
                onOpponentSelected = { opponent -> vm.startSuperGato(opponent) },
                onTutorialClick = { vm.showSuperGatoTutorial() },
                onFamilyGreetingDismiss = { vm.dismissSuperFamilyGreeting() },
                onBackClick = { vm.backToHome() }
            )

            Screen.SUPER_TUTORIAL -> SuperGatoTutorialScreen(
                onStartClick = { vm.startSuperGato(uiState.opponent) },
                onBackClick = { vm.showSuperGatoIntro() }
            )

            Screen.GAME -> {
                GameScreen(
                    uiState = uiState,
                    onCellTap = { index ->
                        if (uiState.vibrationEnabled) {
                            VibrationHelper.vibrate(context, 50)
                        }
                        vm.onCellTap(index)
                    },
                    onSuperCellTap = { boardIndex, cellIndex ->
                        if (uiState.vibrationEnabled) {
                            VibrationHelper.vibrate(context, 45)
                        }
                        vm.onSuperCellTap(boardIndex, cellIndex)
                    },
                    onSuperGreetingDismiss = {
                        onSuperMichiIntroDismissed()
                        vm.dismissSuperGreeting()
                    },
                    onPauseClick = { showPauseDialog = true },
                    onSettingsClick = { showPauseSettingsDialog = true }
                )

                if (showPauseDialog) {
                    PauseDialog(
                        isVolumeEnabled = uiState.musicEnabled,
                        gameTheme = ThemeManager.themeFor(uiState.selectedThemeType),
                        opponent = if (uiState.gameMode == GameMode.SUPER_GATO) {
                            uiState.opponent
                        } else {
                            null
                        },
                        onResume = { showPauseDialog = false },
                        onExitHome = {
                            showPauseDialog = false
                            vm.backToHome()
                        },
                        onOpenSettings = {
                            showPauseDialog = false
                            showPauseSettingsDialog = true
                        },
                        onToggleVolume = {
                            vm.setMusicEnabled(!uiState.musicEnabled)
                        },
                        onOpenInfo = {
                            showPauseDialog = false
                            showInfoDialog = true
                        }
                    )
                }

                if (showPauseSettingsDialog) {
                    PauseSettingsDialog(
                        musicEnabled = uiState.musicEnabled,
                        vibrationEnabled = uiState.vibrationEnabled,
                        gameTheme = ThemeManager.themeFor(uiState.selectedThemeType),
                        onDismiss = { showPauseSettingsDialog = false },
                        onMusicToggle = { vm.setMusicEnabled(!uiState.musicEnabled) },
                        onVibrationToggle = { vm.setVibrationEnabled(!uiState.vibrationEnabled) }
                    )
                }

                if (showInfoDialog) {
                    PauseInfoDialog(
                        gameTheme = ThemeManager.themeFor(uiState.selectedThemeType),
                        opponent = if (uiState.gameMode == GameMode.SUPER_GATO) {
                            uiState.opponent
                        } else {
                            null
                        },
                        onDismiss = { showInfoDialog = false }
                    )
                }
            }

            Screen.RESULT -> ResultScreen(
                uiState = uiState,
                onPlayAgain = {
                    if (uiState.gameMode == GameMode.SUPER_GATO) {
                        vm.startSuperGato(uiState.opponent)
                    } else {
                        vm.startGame()
                    }
                },
                onHome = { vm.backToHome() }
            )

            Screen.SETTINGS -> SettingsScreen(
                musicEnabled = uiState.musicEnabled,
                vibrationEnabled = uiState.vibrationEnabled,
                notificationsEnabled = uiState.notificationsEnabled,
                onMusicToggle = { vm.setMusicEnabled(it) },
                onVibrationToggle = { vm.setVibrationEnabled(it) },
                onNotificationsToggle = onNotificationsToggle,
                onMeetSuperMichisClick = {
                    vm.showSuperGatoIntro()
                    vm.showSuperFamilyGreeting()
                },
                onBackClick = { vm.backToHome() }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun screenTransition(target: Screen): ContentTransform {
    return when (target) {
        Screen.GAME -> {
            (fadeIn() + slideInHorizontally(initialOffsetX = { it / 3 })) togetherWith
                    (fadeOut() + slideOutHorizontally(targetOffsetX = { -it / 5 }))
        }

        Screen.RESULT -> {
            (fadeIn() + slideInVertically(initialOffsetY = { it / 5 })) togetherWith
                    (fadeOut() + slideOutVertically(targetOffsetY = { -it / 6 }))
        }

        Screen.SETTINGS -> {
            (fadeIn() + slideInHorizontally(initialOffsetX = { it / 4 })) togetherWith
                    (fadeOut() + slideOutHorizontally(targetOffsetX = { -it / 6 }))
        }

        Screen.SUPER_INTRO, Screen.SUPER_TUTORIAL -> {
            (fadeIn() + slideInVertically(initialOffsetY = { it / 6 })) togetherWith
                    (fadeOut() + slideOutVertically(targetOffsetY = { -it / 8 }))
        }

        Screen.HOME -> {
            (fadeIn() + slideInHorizontally(initialOffsetX = { -it / 4 })) togetherWith
                    (fadeOut() + slideOutHorizontally(targetOffsetX = { it / 6 }))
        }
    }
}

@Composable
fun GameScreen(
    uiState: GameUiState,
    onCellTap: (Int) -> Unit,
    onSuperCellTap: (Int, Int) -> Unit = { _, _ -> },
    onSuperGreetingDismiss: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val gameTheme = ThemeManager.themeFor(uiState.selectedThemeType)

    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gameTheme.backgroundColor)
            .navigationBarsPadding()
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        ThemeBackgroundLayer(theme = gameTheme)

        if (uiState.gameMode == GameMode.SUPER_GATO) {
            SuperGatoGameContent(
                uiState = uiState,
                gameTheme = gameTheme,
                isLandscape = isLandscape,
                onCellTap = onSuperCellTap,
                onGreetingDismiss = onSuperGreetingDismiss,
                onPauseClick = onPauseClick,
                onSettingsClick = onSettingsClick
            )
        } else if (isLandscape) {
            LandscapeGameContent(
                uiState = uiState,
                gameTheme = gameTheme,
                onCellTap = onCellTap,
                onPauseClick = onPauseClick,
                onSettingsClick = onSettingsClick
            )
        } else {
            PortraitGameContent(
                uiState = uiState,
                gameTheme = gameTheme,
                onCellTap = onCellTap,
                onPauseClick = onPauseClick,
                onSettingsClick = onSettingsClick
            )
        }
    }
}

@Composable
fun PortraitGameContent(
    uiState: GameUiState,
    gameTheme: MichiGameTheme,
    onCellTap: (Int) -> Unit,
    onPauseClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GameHeader(gameTheme)

        Spacer(modifier = Modifier.height(if (isTablet) 26.dp else 20.dp))

        LuzHeader(uiState = uiState)

        Spacer(modifier = Modifier.height(if (isTablet) 34.dp else 26.dp))

        GameBoard(
            board = uiState.board,
            gameTheme = gameTheme,
            onCellTap = onCellTap,
            modifier = Modifier.fillMaxWidth(if (isTablet) 0.62f else 0.9f)
        )

        Spacer(modifier = Modifier.height(if (isTablet) 24.dp else 18.dp))

        GameStatus(uiState = uiState, gameTheme = gameTheme)

        Spacer(modifier = Modifier.height(if (isTablet) 34.dp else 28.dp))

        GameActions(
            gameTheme = gameTheme,
            onPauseClick = onPauseClick,
            onSettingsClick = onSettingsClick
        )
    }
}

@Composable
fun LandscapeGameContent(
    uiState: GameUiState,
    gameTheme: MichiGameTheme,
    onCellTap: (Int) -> Unit,
    onPauseClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (isTablet) 34.dp else 24.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GameHeader(gameTheme)

            Spacer(modifier = Modifier.height(20.dp))

            LuzHeader(uiState = uiState)

            Spacer(modifier = Modifier.height(22.dp))

            GameStatus(uiState = uiState, gameTheme = gameTheme)

            Spacer(modifier = Modifier.height(22.dp))

            GameActions(
                gameTheme = gameTheme,
                onPauseClick = onPauseClick,
                onSettingsClick = onSettingsClick
            )
        }

        Box(
            modifier = Modifier
                .weight(1.25f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            GameBoard(
                board = uiState.board,
                gameTheme = gameTheme,
                onCellTap = onCellTap,
                modifier = Modifier.fillMaxWidth(if (isTablet) 0.72f else 0.82f)
            )
        }
    }
}

@Composable
fun GameHeader(gameTheme: MichiGameTheme? = null) {
    val activeTheme = gameTheme ?: ThemeManager.themeFor(ThemeType.Luz)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            color = activeTheme.secondaryColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = stringResource(R.string.cd_pets),
                    modifier = Modifier.size(16.dp),
                    tint = activeTheme.primaryColor
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontFamily = MichiFont,
                fontSize = 22.sp,
                color = activeTheme.primaryColor
            )
        }
    }
}

@Composable
fun LuzHeader(
    uiState: GameUiState
) {
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.luz_thinking),
                contentDescription = stringResource(R.string.cd_luz_thinking),
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .border(3.dp, MichiPink, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.cat_luz),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MichiSoftBrown
            )

            Spacer(modifier = Modifier.height(4.dp))

//            Text(
//                text = stringResource(R.string.player_2_ai),
//                fontSize = 13.sp,
//                color = MichiTextPrimary
//            )
        }

        Surface(
            modifier = Modifier.offset(y = (-4).dp),
            shape = RoundedCornerShape(14.dp),
            color = MichiWhite,
            shadowElevation = 6.dp
        ) {
            Text(
                text = if (uiState.isAiThinking) {
                    stringResource(R.string.luz_thinking_message)
                } else {
                    stringResource(R.string.luz_observing_message)
                },
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                color = MichiButton,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun GameStatus(
    uiState: GameUiState,
    gameTheme: MichiGameTheme
) {
    Text(
        text = when {
            uiState.winner == Player.X -> stringResource(R.string.status_you_win)
            uiState.winner == Player.O -> stringResource(R.string.status_luz_wins)
            uiState.isDraw -> stringResource(R.string.status_draw)
            uiState.isAiThinking -> stringResource(R.string.status_luz_thinking)
            else -> stringResource(R.string.status_your_turn)
        },
        color = gameTheme.primaryColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.5.sp
    )
}

@Composable
fun GameActions(
    gameTheme: MichiGameTheme? = null,
    onPauseClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionCircleButton(
            label = stringResource(R.string.pause),
            icon = Icons.Filled.Pause,
            gameTheme = gameTheme,
            onClick = onPauseClick
        )

        ActionCircleButton(
            label = stringResource(R.string.settings_title),
            icon = Icons.Filled.Settings,
            gameTheme = gameTheme,
            onClick = onSettingsClick
        )
    }
}

@Composable
fun GameCell(
    value: Player,
    gameTheme: MichiGameTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = gameTheme.secondaryColor
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val cellWidth = maxWidth

            val iconSize = (cellWidth * 0.75f)
                .coerceIn(48.dp, 112.dp)

            when (value) {
                Player.X -> Icon(
                    painter = painterResource(id = gameTheme.xIcon),
                    contentDescription = stringResource(R.string.cd_player_x),
                    modifier = Modifier.size(iconSize),
                    tint = gameTheme.xColor
                )

                Player.O -> {
                    if (gameTheme.useLibraryPawForO) {
                        LuzPawBadge(
                            contentDescription = stringResource(R.string.cd_player_o),
                            modifier = Modifier.size(iconSize),
                            pawColor = gameTheme.oColor
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = gameTheme.oIcon),
                            contentDescription = stringResource(R.string.cd_player_o),
                            modifier = Modifier.size(iconSize),
                            tint = gameTheme.oColor
                        )
                    }
                }

                Player.NONE -> {}
            }
        }
    }
}

@Composable
fun GameBoard(
    board: List<Player>,
    gameTheme: MichiGameTheme,
    onCellTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(30.dp),
        color = gameTheme.boardColor,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col

                        GameCell(
                            value = board[index],
                            gameTheme = gameTheme,
                            onClick = { onCellTap(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LuzPawBadge(
    contentDescription: String,
    modifier: Modifier = Modifier,
    pawColor: Color
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MichiWhite.copy(alpha = 0.86f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Pets,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(0.72f),
            tint = pawColor
        )
    }
}

@Composable
fun ActionCircleButton(
    label: String,
    icon: ImageVector,
    gameTheme: MichiGameTheme? = null,
    onClick: () -> Unit
) {
    val activeTheme = gameTheme ?: ThemeManager.themeFor(ThemeType.Luz)
    val themedMode = activeTheme.themeType != ThemeType.Luz
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            color = if (themedMode) activeTheme.boardColor else MichiWhite,
            shadowElevation = 6.dp,
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(22.dp),
                    tint = if (themedMode) activeTheme.pauseAccentColor else MichiSoftBrown
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = if (themedMode) activeTheme.pauseTextColor.copy(alpha = 0.82f) else MichiTextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    MichiXOTheme {
        GameScreen(
            uiState = GameUiState(
                screen = Screen.GAME,
                board = listOf(
                    Player.X, Player.NONE, Player.O,
                    Player.NONE, Player.X, Player.NONE,
                    Player.NONE, Player.NONE, Player.O
                )
            ),
            onCellTap = {}
        )
    }
}
