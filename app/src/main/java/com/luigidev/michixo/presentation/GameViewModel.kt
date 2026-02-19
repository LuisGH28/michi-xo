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
            // If I have already finished the game, ignore taps.
            if (state.winner != null || state.isDraw) return@update state

            val nextBoard = engine.makeMove(state.board, index, state.currentTurn)

            if (nextBoard == state.board) return@update state

            val winResult = Rules.checkWinner(nextBoard)
            val draw = Rules.isDraw(nextBoard)

            state.copy(
                board = nextBoard,
                winner = winResult?.player,
                winLine = winResult?.line,
                isDraw = draw,
                currentTurn = if (state.currentTurn == Player.X) Player.O else Player.X
            )
        }
    }

    fun reset() {
        _uiState.value = GameUiState(board = engine.newBoard())
    }
}
