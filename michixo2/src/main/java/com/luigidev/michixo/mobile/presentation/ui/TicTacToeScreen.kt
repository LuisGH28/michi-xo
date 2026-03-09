package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.presentation.GameUiState
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.Screen
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme
import com.luigidev.michixo.model.Player


@Composable
fun TicTacToeScreen(vm: GameViewModel) {
    val state = vm.uiState.collectAsState().value

    TicTacToeContent(
        state = state,
        onPlayClick = { vm.startGame() },
        onCellTap = vm::onCellTap,
        onReset = vm::reset,
        onBackToHome = vm::backToHome
    )
}

@Composable
fun TicTacToeContent(
    state: GameUiState,
    onPlayClick: () -> Unit,
    onCellTap: (Int) -> Unit,
    onReset: () -> Unit,
    onBackToHome: () -> Unit
) {
    if (state.screen == Screen.HOME) {
        HomeScreenContent(
            onPlayClick = onPlayClick
        )
        return
    }

    val gameFinished = state.winner != null || state.isDraw

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = when {
                    state.winner == state.humanPlayer -> "Ganaste"
                    state.winner == state.aiPlayer -> "Ganó Luz"
                    state.isDraw -> "Empate"
                    state.isAiThinking -> "Luz pensando..."
                    else -> "Turno: ${state.currentTurn}"
                },
                color = when (state.currentTurn) {
                    Player.X -> MaterialTheme.colorScheme.primary
                    Player.O -> Color(0xFF81C784)
                    else -> Color(0xFF6D4C41)
                },
                fontFamily = MichiFont,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Board(
                board = state.board,
                winLine = state.winLine,
                onTap = onCellTap
            )
        }

        if (gameFinished) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 220.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.onBackground)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when {
                            state.winner == state.humanPlayer -> "Ganaste"
                            state.winner == state.aiPlayer -> "Ganó Luz"
                            else -> "Empate"
                        },
                        fontFamily = MichiFont,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = onReset,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Otra vez",
                            maxLines = 1,
                            fontFamily = MichiFont,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = onBackToHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF80AB),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Regresar",
                            maxLines = 1,
                            fontFamily = MichiFont,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Board(
    board: List<Player>,
    winLine: List<Int>?,
    onTap: (Int) -> Unit
) {
    Box {
        Column {
            for (r in 0..2) {
                Row {
                    for (c in 0..2) {
                        val index = r * 3 + c
                        Button(
                            onClick = { onTap(index) },
                            modifier = Modifier
                                .size(64.dp)
                                .padding(4.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = when (board[index]) {
                                    Player.X -> "X"
                                    Player.O -> "O"
                                    Player.NONE -> ""
                                },
                                fontFamily = MichiFont,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }
        }

        if (winLine != null) {
            val winLineColor = MaterialTheme.colorScheme.onPrimary

            Canvas(modifier = Modifier.matchParentSize()) {
                val cellSize = size.width / 3f

                val (start, end) = when (winLine) {
                    listOf(0, 1, 2) -> Offset(0f, cellSize / 2) to Offset(size.width, cellSize / 2)
                    listOf(3, 4, 5) -> Offset(0f, cellSize * 1.5f) to Offset(size.width, cellSize * 1.5f)
                    listOf(6, 7, 8) -> Offset(0f, cellSize * 2.5f) to Offset(size.width, cellSize * 2.5f)

                    listOf(0, 3, 6) -> Offset(cellSize / 2, 0f) to Offset(cellSize / 2, size.height)
                    listOf(1, 4, 7) -> Offset(cellSize * 1.5f, 0f) to Offset(cellSize * 1.5f, size.height)
                    listOf(2, 5, 8) -> Offset(cellSize * 2.5f, 0f) to Offset(cellSize * 2.5f, size.height)

                    listOf(0, 4, 8) -> Offset(0f, 0f) to Offset(size.width, size.height)
                    listOf(2, 4, 6) -> Offset(size.width, 0f) to Offset(0f, size.height)

                    else -> return@Canvas
                }

                drawLine(
                    color = winLineColor,
                    start = start,
                    end = end,
                    strokeWidth = 8f
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TicTacToeScreenPreview() {
    MichiXOTheme {
        TicTacToeContent(
            state = GameUiState(
                screen = Screen.GAME,
                board = listOf(
                    Player.X, Player.O, Player.X,
                    Player.NONE, Player.O, Player.NONE,
                    Player.X, Player.NONE, Player.O
                ),
                currentTurn = Player.X,
                winner = null,
                isDraw = false,
                isAiThinking = false,
                winLine = null
            ),
            onPlayClick = {},
            onCellTap = {},
            onReset = {},
            onBackToHome = {}
        )
    }
}