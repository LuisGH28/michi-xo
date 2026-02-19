package com.luigidev.michixo.presentation

import androidx.lifecycle.ViewModel
import com.luigidev.michixo.domain.AiPlayer
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

    private val ai = AiPlayer()

    fun onCellTap(index: Int) {
        _uiState.update { state ->
            if (state.winner != null || state.isDraw) return@update state

            if (state.currentTurn != Player.X) return@update state

            val boardAfterHuman = engine.makeMove(state.board, index, Player.X)
            if (boardAfterHuman == state.board) return@update state

            val humanWin = Rules.checkWinner(boardAfterHuman)
            val humanDraw = Rules.isDraw(boardAfterHuman)

            if (humanWin != null || humanDraw) {
                return@update state.copy(
                    board = boardAfterHuman,
                    winner = humanWin?.player,
                    winLine = humanWin?.line,
                    isDraw = humanDraw,
                    currentTurn = Player.X
                )
            }

            val aiMove = ai.chooseMove(boardAfterHuman, Player.O)
            if (aiMove == null) {
                return@update state.copy(
                    board = boardAfterHuman,
                    currentTurn = Player.X
                )
            }

            val boardAfterAi = engine.makeMove(boardAfterHuman, aiMove, Player.O)
            val aiWin = Rules.checkWinner(boardAfterAi)
            val aiDraw = Rules.isDraw(boardAfterAi)

            state.copy(
                board = boardAfterAi,
                winner = aiWin?.player,
                winLine = aiWin?.line,
                isDraw = aiDraw,
                currentTurn = Player.X
            )
        }
    }

    fun reset() {
        _uiState.value = GameUiState(board = engine.newBoard(), currentTurn = Player.X)
    }
}
