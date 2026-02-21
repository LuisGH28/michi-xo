package com.luigidev.michixo.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.luigidev.michixo.model.Player
import com.luigidev.michixo.presentation.GameViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import com.luigidev.michixo.presentation.Screen
import com.luigidev.michixo.presentation.theme.MichiFont

@Composable
fun TicTacToeScreen(vm: GameViewModel) {
    val state = vm.uiState.collectAsState().value

    if(state.screen == Screen.HOME){
        HomeScreen(vm)
        return
    }

    val gameFinished = state.winner != null || state.isDraw

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
        ) {

        // PRINCIPAL CONTENT
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
                    state.isAiThinking -> "Luz Pensando.."
                    else -> "Turno: ${state.currentTurn}"
                },
                color = when (state.currentTurn){
                    Player.X -> MaterialTheme.colors.primary
                    Player.O -> Color(0xFF81C784)
                    else -> Color(0xFF6D4C41)
                },
                fontFamily = MichiFont,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(8.dp))

            Board(
                board = state.board,
                winLine = state.winLine,
                onTap = vm::onCellTap
            )
        }

        // OVERLAY (POPUP)
        if (gameFinished) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000))
                    .clickable { /* tap block on the board */ },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 200.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colors.onBackground)
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

                    Spacer(Modifier.height(6.dp))

                    Button(
                        onClick = { vm.reset() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        colors = androidx.wear.compose.material.ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            "Otra vez", maxLines = 1,
                            fontFamily = MichiFont,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Button(
                        onClick = { vm.backToHome() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        colors = androidx.wear.compose.material.ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFFF80AB),
                            contentColor = Color.Black
                        )
                    ){
                        Text(
                            "Regresar", maxLines = 1,
                            fontFamily = MichiFont,
                            fontSize = 20.sp
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
                                .size(56.dp)
                                .padding(2.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = androidx.wear.compose.material.ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.onSurface
                            )
                        ) {
                            Text(
                                text = when (board[index]) {
                                    Player.X -> "X"
                                    Player.O -> "O"
                                    Player.NONE -> ""
                                }
                            )
                        }

                    }
                }
            }
        }

        if (winLine != null) {
            val winLineColor = MaterialTheme.colors.onPrimary
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
