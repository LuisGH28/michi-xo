package com.luigidev.michixo.mobile.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.CatOpponent
import com.luigidev.michixo.mobile.presentation.GameMode
import com.luigidev.michixo.mobile.presentation.GameUiState
import com.luigidev.michixo.mobile.presentation.Screen
import com.luigidev.michixo.mobile.presentation.theme.MichiBoard
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiDeepPink
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiO
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme
import com.luigidev.michixo.model.Player

@Composable
fun ResultScreen(
    uiState: GameUiState,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    var showBoardDialog by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = configuration.smallestScreenWidthDp >= 600

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MichiSoftPink)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        if (isTablet && isLandscape) {
            TabletLandscapeResultContent(
                uiState = uiState,
                onPlayAgain = onPlayAgain,
                onHome = onHome,
                onViewBoard = { showBoardDialog = true }
            )
        } else {
            PortraitResultContent(
                uiState = uiState,
                isTablet = isTablet,
                onPlayAgain = onPlayAgain,
                onHome = onHome,
                onViewBoard = { showBoardDialog = true }
            )
        }
    }

    if (showBoardDialog) {
        AlertDialog(
            onDismissRequest = { showBoardDialog = false },
            confirmButton = {},
            title = {
                Text(
                    text = stringResource(R.string.final_board),
                    fontFamily = MichiFont,
                    color = MichiSoftBrown,
                    fontSize = 22.sp
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ResultBoard(
                        board = uiState.board,
                        winLine = uiState.winLine,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showBoardDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MichiButton,
                            contentColor = MichiWhite
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        )
    }
}

@Composable
fun PortraitResultContent(
    uiState: GameUiState,
    isTablet: Boolean,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
    onViewBoard: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ResultTitle()

        Spacer(modifier = Modifier.height(22.dp))

        ResultImageCard(
            uiState = uiState,
            modifier = Modifier.fillMaxWidth(),
            imageHeight = if (isTablet) 380.dp else 320.dp,
            contentScale = if (isTablet) ContentScale.Fit else ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(26.dp))

        ResultText(uiState = uiState)

        Spacer(modifier = Modifier.height(12.dp))

        ResultMessage(uiState = uiState)

        Spacer(modifier = Modifier.height(26.dp))

        ResultButtons(
            onPlayAgain = onPlayAgain,
            onViewBoard = onViewBoard,
            onHome = onHome
        )
    }
}

@Composable
fun TabletLandscapeResultContent(
    uiState: GameUiState,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
    onViewBoard: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1.25f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ResultImageCard(
                uiState = uiState,
                modifier = Modifier.fillMaxWidth(),
                imageHeight = 360.dp,
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .weight(0.85f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ResultTitle()

            Spacer(modifier = Modifier.height(20.dp))

            ResultText(uiState = uiState)

            Spacer(modifier = Modifier.height(12.dp))

            ResultMessage(uiState = uiState)

            Spacer(modifier = Modifier.height(28.dp))

            ResultButtons(
                onPlayAgain = onPlayAgain,
                onViewBoard = onViewBoard,
                onHome = onHome
            )
        }
    }
}

@Composable
fun ResultTitle() {
    Text(
        text = stringResource(R.string.match_result),
        fontFamily = MichiFont,
        fontSize = 30.sp,
        color = MichiSoftBrown,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ResultImageCard(
    uiState: GameUiState,
    modifier: Modifier = Modifier,
    imageHeight: Dp,
    contentScale: ContentScale
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        color = MichiDeepPink
    ) {
        Box(
            modifier = Modifier.padding(6.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            uiState.resultImageRes?.let { imageRes ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = stringResource(R.string.cd_result_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clip(RoundedCornerShape(26.dp)),
                    contentScale = contentScale
                )
            }

            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .size(56.dp),
                shape = CircleShape,
                color = MichiButton,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = stringResource(R.string.cd_winner),
                        tint = MichiWhite,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResultText(
    uiState: GameUiState
) {
    Text(
        text = when {
            uiState.winner == Player.X -> stringResource(R.string.result_you_win)
            uiState.winner == Player.O && uiState.gameMode == GameMode.SUPER_GATO ->
                stringResource(R.string.result_opponent_wins, uiState.opponent.resultName())
            uiState.winner == Player.O -> stringResource(R.string.result_luz_wins)
            uiState.isDraw -> stringResource(R.string.result_draw)
            else -> stringResource(R.string.match_result_fallback)
        },
        fontFamily = MichiFont,
        fontSize = 38.sp,
        color = MichiButton,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ResultMessage(
    uiState: GameUiState
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MichiPink
    ) {
        Text(
            text = when {
                uiState.gameMode == GameMode.SUPER_GATO && uiState.resultMessageRes != null ->
                    stringResource(uiState.resultMessageRes)
                uiState.winner == Player.X -> stringResource(R.string.result_msg_you_win)
                uiState.winner == Player.O -> stringResource(R.string.result_msg_luz_wins)
                uiState.isDraw -> stringResource(R.string.result_msg_draw)
                else -> stringResource(R.string.good_game_human)
            },
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            color = MichiSoftBrown,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CatOpponent.resultName(): String {
    return when (this) {
        CatOpponent.LILY -> stringResource(R.string.opponent_lily)
        CatOpponent.COCO -> stringResource(R.string.opponent_coco)
        CatOpponent.SALEM -> stringResource(R.string.opponent_salem)
    }
}

@Composable
fun ResultButtons(
    onPlayAgain: () -> Unit,
    onViewBoard: () -> Unit,
    onHome: () -> Unit
) {
    Button(
        onClick = onPlayAgain,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MichiButton,
            contentColor = MichiSoftPink
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.play_again),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.play_again),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
        onClick = onViewBoard,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MichiPink,
            contentColor = MichiSoftBrown
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Visibility,
                contentDescription = stringResource(R.string.view_board),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.view_board),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
        onClick = onHome,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MichiDeepPink,
            contentColor = MichiSoftBrown
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = stringResource(R.string.home),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.home),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ResultBoard(
    board: List<Player>,
    winLine: List<Int>?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(24.dp),
        color = MichiBoard
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col

                        ResultBoardCell(
                            value = board[index],
                            highlighted = winLine?.contains(index) == true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultBoardCell(
    value: Player,
    highlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(14.dp),
        color = if (highlighted) MichiPink else MichiWhite
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val cellWidth = maxWidth

            val iconSize = (cellWidth * 0.50f)
                .coerceIn(28.dp, 54.dp)

            when (value) {
                Player.X -> Icon(
                    painter = painterResource(id = R.drawable.ic_yarn),
                    contentDescription = stringResource(R.string.cd_player_x),
                    modifier = Modifier.size(iconSize),
                    tint = Color.Unspecified
                )

                Player.O -> Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = stringResource(R.string.cd_player_o),
                    modifier = Modifier.size(iconSize),
                    tint = MichiO
                )

                Player.NONE -> {}
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResultScreenPreview() {
    MichiXOTheme {
        ResultScreen(
            uiState = GameUiState(
                screen = Screen.RESULT,
                board = listOf(
                    Player.X, Player.X, Player.X,
                    Player.O, Player.O, Player.NONE,
                    Player.NONE, Player.NONE, Player.NONE
                ),
                winner = Player.X,
                winLine = listOf(0, 1, 2),
                resultTitle = stringResource(R.string.preview_you_win),
                resultMessage = stringResource(R.string.preview_luz_sad),
                resultImageRes = R.drawable.luz_hs
            ),
            onPlayAgain = {},
            onHome = {}
        )
    }
}
