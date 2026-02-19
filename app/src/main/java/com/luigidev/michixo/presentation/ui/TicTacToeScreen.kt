package com.luigidev.michixo.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.luigidev.michixo.model.Player
import com.luigidev.michixo.presentation.GameViewModel

@Composable
fun TicTacToeScreen(vm: GameViewModel) {
    val state = vm.uiState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when {
                state.winner != null -> "Ganó: ${state.winner}"
                state.isDraw -> "Empate"
                else -> "Turno: ${state.currentTurn}"
            }
        )

        Spacer(Modifier.height(8.dp))

        Board(board = state.board, onTap = vm::onCellTap)

        Spacer(Modifier.height(8.dp))

        Button(onClick = { vm.reset() }) {
            Text("Reiniciar")
        }
    }
}

@Composable
private fun Board(board: List<Player>, onTap: (Int) -> Unit) {
    Column {
        for (r in 0..2) {
            Row {
                for (c in 0..2) {
                    val index = r * 3 + c
                    Button(
                        onClick = { onTap(index) },
                        modifier = Modifier.size(56.dp).padding(2.dp)
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
}
