package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.GameUiState
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.Screen
import com.luigidev.michixo.mobile.presentation.theme.MichiBoard
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiO
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme
import com.luigidev.michixo.model.Player
import androidx.activity.compose.BackHandler

@Composable
fun TicTacToeScreen(
    vm: GameViewModel,
    onExitApp: () -> Unit) {
    val uiState by vm.uiState.collectAsState()

    var showPauseDialog by remember { mutableStateOf(false) }
    var showPauseSettingsDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var isVolumeEnabled by remember { mutableStateOf(true) }
    var isVibrationEnabled by remember { mutableStateOf(true) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }

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

            uiState.screen == Screen.GAME -> {
                showPauseDialog = true
            }
        }
    }

    when (uiState.screen) {
        Screen.HOME -> HomeScreen(vm)

        Screen.GAME -> {
            GameScreen(
                uiState = uiState,
                onCellTap = vm::onCellTap,
                onPauseClick = { showPauseDialog = true },
                onSettingsClick = { showPauseSettingsDialog = true }
            )

            if (showPauseDialog) {
                PauseDialog(
                    isVolumeEnabled = isVolumeEnabled,
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
                        isVolumeEnabled = !isVolumeEnabled
                    },
                    onOpenInfo = {
                        showPauseDialog = false
                        showInfoDialog = true
                    }
                )
            }

            if (showPauseSettingsDialog) {
                PauseSettingsDialog(
                    musicEnabled = isVolumeEnabled,
                    vibrationEnabled = isVibrationEnabled,
                    onDismiss = { showPauseSettingsDialog = false },
                    onMusicToggle = { isVolumeEnabled = !isVolumeEnabled },
                    onVibrationToggle = { isVibrationEnabled = !isVibrationEnabled }
                )
            }

            if (showInfoDialog) {
                PauseInfoDialog(
                    onDismiss = { showInfoDialog = false }
                )
            }
        }

        Screen.RESULT -> ResultScreen(
            uiState = uiState,
            onPlayAgain = { vm.startGame() },
            onHome = { vm.backToHome() }
        )

        Screen.SETTINGS -> SettingsScreen(
            musicEnabled = isVolumeEnabled,
            vibrationEnabled = isVibrationEnabled,
            notificationsEnabled = isNotificationsEnabled,
            onMusicToggle = { isVolumeEnabled = it },
            onVibrationToggle = { isVibrationEnabled = it },
            onNotificationsToggle = { isNotificationsEnabled = it },
            onBackClick = { vm.backToHome() }
        )
    }
}

@Composable
fun GameScreen(
    uiState: GameUiState,
    onCellTap: (Int) -> Unit,
    onPauseClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MichiSoftPink)
            .navigationBarsPadding()
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    color = MichiPink
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Pets,
                            contentDescription = stringResource(R.string.cd_pets),
                            modifier = Modifier.size(16.dp),
                            tint = MichiSoftBrown
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
                        color = MichiButton
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

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

                    Text(
                        text = stringResource(R.string.player_2_ai),
                        fontSize = 13.sp,
                        color = MichiTextPrimary
                    )
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

            Spacer(modifier = Modifier.height(26.dp))

            GameBoard(
                board = uiState.board,
                onCellTap = onCellTap,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = when {
                    uiState.winner == Player.X -> stringResource(R.string.status_you_win)
                    uiState.winner == Player.O -> stringResource(R.string.status_luz_wins)
                    uiState.isDraw -> stringResource(R.string.status_draw)
                    uiState.isAiThinking -> stringResource(R.string.status_luz_thinking)
                    else -> stringResource(R.string.status_your_turn)
                },
                color = MichiButton,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionCircleButton(
                    label = stringResource(R.string.pause),
                    icon = Icons.Filled.Pause,
                    onClick = onPauseClick
                )

                ActionCircleButton(
                    label = stringResource(R.string.settings_title),
                    icon = Icons.Filled.Settings,
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
fun GameBoard(
    board: List<Player>,
    onCellTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(30.dp),
        color = MichiBoard,
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
fun GameCell(
    value: Player,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MichiWhite
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            when (value) {
                Player.X -> Text(
                    text = "✕",
                    fontSize = 34.sp,
                    color = MichiButton,
                    fontWeight = FontWeight.Bold
                )

                Player.O -> Text(
                    text = "◯",
                    fontSize = 34.sp,
                    color = MichiO,
                    fontWeight = FontWeight.Bold
                )

                Player.NONE -> {}
            }
        }
    }
}

@Composable
fun ActionCircleButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            color = MichiWhite,
            shadowElevation = 6.dp,
            onClick = onClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(22.dp),
                    tint = MichiSoftBrown
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = MichiTextPrimary,
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