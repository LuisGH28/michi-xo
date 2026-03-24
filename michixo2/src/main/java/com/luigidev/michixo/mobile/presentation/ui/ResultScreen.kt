package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MichiSoftPink)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.match_result),
                fontFamily = MichiFont,
                fontSize = 30.sp,
                color = MichiSoftBrown,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(22.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
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
                                .height(320.dp)
                                .clip(RoundedCornerShape(26.dp)),
                            contentScale = ContentScale.Crop
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

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = when {
                    uiState.winner == Player.X -> stringResource(R.string.result_you_win)
                    uiState.winner == Player.O -> stringResource(R.string.result_luz_wins)
                    uiState.isDraw -> stringResource(R.string.result_draw)
                    else -> stringResource(R.string.match_result_fallback)
                },
                fontFamily = MichiFont,
                fontSize = 38.sp,
                color = MichiButton,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MichiPink
            ) {
                Text(
                    text = when {
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

            Spacer(modifier = Modifier.height(26.dp))

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
                onClick = { showBoardDialog = true },
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
        Box(
            contentAlignment = Alignment.Center
        ) {
            when (value) {
                Player.X -> Text(
                    text = "✕",
                    fontSize = 28.sp,
                    color = MichiButton,
                    fontWeight = FontWeight.Bold
                )

                Player.O -> Text(
                    text = "◯",
                    fontSize = 28.sp,
                    color = MichiO,
                    fontWeight = FontWeight.Bold
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