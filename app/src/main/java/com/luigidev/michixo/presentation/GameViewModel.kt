package com.luigidev.michixo.presentation

import androidx.lifecycle.ViewModel
import com.luigidev.michixo.domain.GameEngine
import com.luigidev.michixo.domain.Rules
import com.luigidev.michixo.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel(
    private val engine: GameEngine = GameEngine()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(board = engine.newBoard()))
    val uiState: StateFlow<GameUiState> = _uiState

    fun onCellTap(index: Int) {
        _uiState.update { state ->
            // si ya terminó el juego, ignorar taps
            if (state.winner != null || state.isDraw) return

            val nextBoard = engine.makeMove(state.board, index, state.currentTurn)

            // si no cambió, fue un movimiento inválido
            if (nextBoard == state.board) return

            val winner = Rules.winner(nextBoard)
            val draw = Rules.isDraw(nextBoard)

            state.copy(
                board = nextBoard,
                winner = winner,
                isDraw = draw,
                currentTurn = if (state.currentTurn == Player.X) Player.O else Player.X
            )
        }
    }

    fun reset() {
        _uiState.value = GameUiState(board = engine.newBoard())
    }
}
