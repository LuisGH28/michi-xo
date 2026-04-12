package com.luigidev.michixo.mobile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.model.GameResult
import com.luigidev.michixo.domain.AiPlayer
import com.luigidev.michixo.domain.GameEngine
import com.luigidev.michixo.domain.Rules
import com.luigidev.michixo.mobile.network.RemoteAiMapper
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
    var onRemoteMoveRequested: ((String) -> Unit)? = null

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    fun setGameMode(gameMode: GameMode) {
        _uiState.update { it.copy(gameMode = gameMode) }
    }

    fun setRemoteAiConnected(connected: Boolean) {
        _uiState.update {
            it.copy(
                remoteAiConnected = connected,
                remoteAiStatus = if (connected) "Remote AI connected" else "Remote AI disconnected"
            )
        }
    }

    fun startGame() {
        val state = _uiState.value

        _uiState.value = GameUiState(
            screen = Screen.GAME,
            board = engine.newBoard(),
            currentTurn = Player.X,
            difficulty = state.difficulty,
            gameMode = state.gameMode,
            musicEnabled = state.musicEnabled,
            vibrationEnabled = state.vibrationEnabled,
            notificationsEnabled = state.notificationsEnabled,
            remoteAiConnected = state.remoteAiConnected,
            remoteAiStatus = state.remoteAiStatus,
            resultTitle = "",
            resultMessage = "",
            resultImageRes = null
        )

        if (state.gameMode == GameMode.AI_VS_AI_REMOTE) {
            startAiVsAiMatch()
        }
    }

    fun goToSettings() {
        _uiState.update { it.copy(screen = Screen.SETTINGS) }
    }

    fun backToHome() {
        val state = _uiState.value
        _uiState.value = GameUiState(
            screen = Screen.HOME,
            difficulty = state.difficulty,
            gameMode = state.gameMode,
            musicEnabled = state.musicEnabled,
            vibrationEnabled = state.vibrationEnabled,
            notificationsEnabled = state.notificationsEnabled,
            remoteAiConnected = state.remoteAiConnected,
            remoteAiStatus = state.remoteAiStatus
        )
    }

    fun onCellTap(index: Int) {
        val state = _uiState.value
        if (state.gameMode == GameMode.AI_VS_AI_REMOTE) return

        var boardAfterHuman: List<Player>? = null

        _uiState.update { current ->
            if (current.screen != Screen.GAME) return@update current
            if (current.winner != null || current.isDraw) return@update current
            if (current.isAiThinking) return@update current

            val next = engine.makeMove(current.board, index, Player.X)
            if (next == current.board) return@update current

            val humanWin = Rules.checkWinner(next)
            val humanDraw = Rules.isDraw(next)

            if (humanWin != null || humanDraw) {
                val result = when {
                    humanDraw -> GameResult.DRAW
                    humanWin?.player == Player.X -> GameResult.WIN
                    else -> GameResult.NONE
                }

                onGameFinished?.invoke(result)

                return@update current.copy(
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

            current.copy(
                board = next,
                currentTurn = Player.O,
                isAiThinking = true
            )
        }

        val baseBoard = boardAfterHuman ?: return

        viewModelScope.launch {
            delay(450)

            _uiState.update { current ->
                if (current.winner != null || current.isDraw) return@update current

                val ai = AiPlayer(current.difficulty)
                val aiMove = ai.chooseMove(baseBoard, Player.O)
                    ?: return@update current.copy(isAiThinking = false)

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

                current.copy(
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
                        else -> current.resultImageRes
                    }
                )
            }
        }
    }

    fun startAiVsAiMatch() {
        viewModelScope.launch {
            delay(500)
            playLocalAiTurn()
        }
    }

    private fun playLocalAiTurn() {
        val state = _uiState.value
        if (state.screen != Screen.GAME) return
        if (state.winner != null || state.isDraw) return
        if (state.currentTurn != Player.X) return

        _uiState.update { it.copy(isAiThinking = true) }

        viewModelScope.launch {
            delay(650)

            val current = _uiState.value
            val localAi = AiPlayer(current.difficulty)
            val move = localAi.chooseMove(current.board, Player.X)

            if (move == null) {
                _uiState.update { it.copy(isAiThinking = false) }
                return@launch
            }

            val newBoard = engine.makeMove(current.board, move, Player.X)
            val win = Rules.checkWinner(newBoard)
            val draw = Rules.isDraw(newBoard)

            if (win != null || draw) {
                val result = when {
                    draw -> GameResult.DRAW
                    win?.player == Player.X -> GameResult.WIN
                    else -> GameResult.NONE
                }
                onGameFinished?.invoke(result)

                _uiState.update {
                    it.copy(
                        board = newBoard,
                        winner = win?.player,
                        winLine = win?.line,
                        isDraw = draw,
                        currentTurn = Player.X,
                        isAiThinking = false,
                        screen = Screen.RESULT,
                        resultImageRes = when {
                            draw -> R.drawable.luz_draw
                            win?.player == Player.X -> R.drawable.luz_sad
                            else -> null
                        }
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    board = newBoard,
                    currentTurn = Player.O,
                    isAiThinking = false
                )
            }

            requestRemoteAiMove()
        }
    }

    private fun requestRemoteAiMove() {
        val state = _uiState.value
        if (state.screen != Screen.GAME) return
        if (state.winner != null || state.isDraw) return
        if (state.currentTurn != Player.O) return

        _uiState.update {
            it.copy(
                isAiThinking = true,
                remoteAiStatus = "Remote AI is thinking..."
            )
        }

        val payload = RemoteAiMapper.buildMoveRequest(
            board = state.board,
            aiSymbol = "O",
            humanSymbol = "X"
        )

        onRemoteMoveRequested?.invoke(payload)
    }

    fun applyRemoteMove(index: Int) {
        val state = _uiState.value
        if (state.screen != Screen.GAME) return
        if (state.winner != null || state.isDraw) return
        if (state.currentTurn != Player.O) return

        viewModelScope.launch {
            delay(650)

            val newBoard = engine.makeMove(state.board, index, Player.O)
            val win = Rules.checkWinner(newBoard)
            val draw = Rules.isDraw(newBoard)

            if (win != null || draw) {
                val result = when {
                    draw -> GameResult.DRAW
                    win?.player == Player.O -> GameResult.LOSE
                    else -> GameResult.NONE
                }
                onGameFinished?.invoke(result)

                _uiState.update {
                    it.copy(
                        board = newBoard,
                        winner = win?.player,
                        winLine = win?.line,
                        isDraw = draw,
                        currentTurn = Player.O,
                        isAiThinking = false,
                        screen = Screen.RESULT,
                        remoteAiStatus = "Remote AI finished move",
                        resultImageRes = when {
                            draw -> R.drawable.luz_draw
                            win?.player == Player.O -> R.drawable.luz_winner
                            else -> null
                        }
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    board = newBoard,
                    currentTurn = Player.X,
                    isAiThinking = false,
                    remoteAiStatus = "Remote AI played"
                )
            }

            playLocalAiTurn()
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