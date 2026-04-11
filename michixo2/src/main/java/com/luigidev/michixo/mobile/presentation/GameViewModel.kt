package com.luigidev.michixo.mobile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.model.GameResult
import com.luigidev.michixo.domain.AiPlayer
import com.luigidev.michixo.domain.GameEngine
import com.luigidev.michixo.domain.Rules
import com.luigidev.michixo.model.Player
import com.luigidev.michixo_core.model.Difficulty
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val engine: GameEngine = GameEngine()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(board = engine.newBoard()))
    val uiState: StateFlow<GameUiState> = _uiState

    var onGameFinished: ((GameResult) -> Unit)? = null

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.update { state ->
            state.copy(difficulty = difficulty)
        }
    }

    fun startGame() {
        val state = _uiState.value

        _uiState.value = GameUiState(
            screen = Screen.GAME,
            board = engine.newBoard(),
            currentTurn = Player.X,
            difficulty = state.difficulty,
            musicEnabled = state.musicEnabled,
            vibrationEnabled = state.vibrationEnabled,
            notificationsEnabled = state.notificationsEnabled,
            resultTitle = "",
            resultMessage = "",
            resultImageRes = null
        )
    }

    fun goToSettings() {
        _uiState.update { state ->
            state.copy(screen = Screen.SETTINGS)
        }
    }

    fun backToHome() {
        val state = _uiState.value
        _uiState.value = GameUiState(
            screen = Screen.HOME,
            difficulty = state.difficulty,
            musicEnabled = state.musicEnabled,
            vibrationEnabled = state.vibrationEnabled,
            notificationsEnabled = state.notificationsEnabled
        )
    }

    fun onCellTap(index: Int) {
        var boardAfterHuman: List<Player>? = null

        _uiState.update { state ->
            if (state.screen != Screen.GAME) return@update state
            if (state.winner != null || state.isDraw) return@update state
            if (state.isAiThinking) return@update state

            val next = engine.makeMove(state.board, index, Player.X)
            if (next == state.board) return@update state

            val humanWin = Rules.checkWinner(next)
            val humanDraw = Rules.isDraw(next)

            if (humanWin != null || humanDraw) {
                val result = when {
                    humanDraw -> GameResult.DRAW
                    humanWin?.player == Player.X -> GameResult.WIN
                    else -> GameResult.NONE
                }

                onGameFinished?.invoke(result)

                return@update state.copy(
                    board = next,
                    winner = humanWin?.player,
                    winLine = humanWin?.line,
                    isDraw = humanDraw,
                    currentTurn = Player.X,
                    isAiThinking = false,
                    screen = Screen.RESULT,
                    resultTitle = "",
                    resultMessage = "",
                    resultImageRes = when {
                        humanDraw -> R.drawable.luz_draw
                        humanWin?.player == Player.X -> R.drawable.luz_sad
                        else -> null
                    }
                )
            }

            boardAfterHuman = next

            state.copy(
                board = next,
                currentTurn = Player.O,
                isAiThinking = true
            )
        }

        val baseBoard = boardAfterHuman ?: return

        viewModelScope.launch {
            delay(450)

            _uiState.update { state ->
                if (state.winner != null || state.isDraw) return@update state

                val ai = AiPlayer(state.difficulty)
                val aiMove = ai.chooseMove(baseBoard, Player.O)
                    ?: return@update state.copy(isAiThinking = false)

                val boardAfterAi = engine.makeMove(baseBoard, aiMove, Player.O)
                val aiWin = Rules.checkWinner(boardAfterAi)
                val aiDraw = Rules.isDraw(boardAfterAi)

                if (aiWin != null || aiDraw) {
                    val result = when {
                        aiDraw -> GameResult.DRAW
                        aiWin?.player == Player.O -> GameResult.LOSE
                        else -> GameResult.NONE
                    }

                    onGameFinished?.invoke(result)
                }

                state.copy(
                    board = boardAfterAi,
                    winner = aiWin?.player,
                    winLine = aiWin?.line,
                    isDraw = aiDraw,
                    currentTurn = Player.X,
                    isAiThinking = false,
                    screen = if (aiWin != null || aiDraw) Screen.RESULT else Screen.GAME,
                    resultTitle = "",
                    resultMessage = "",
                    resultImageRes = when {
                        aiDraw -> R.drawable.luz_draw
                        aiWin?.player == Player.O -> R.drawable.luz_winner
                        else -> state.resultImageRes
                    }
                )
            }
        }
    }

    fun reset() {
        _uiState.update { state ->
            state.copy(
                board = engine.newBoard(),
                currentTurn = Player.X,
                winner = null,
                winLine = null,
                isDraw = false,
                isAiThinking = false,
                resultTitle = "",
                resultMessage = "",
                resultImageRes = null
            )
        }
    }

    fun setMusicEnabled(enabled: Boolean) {
        _uiState.update { it.copy(musicEnabled = enabled) }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        _uiState.update { it.copy(vibrationEnabled = enabled) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
    }
}