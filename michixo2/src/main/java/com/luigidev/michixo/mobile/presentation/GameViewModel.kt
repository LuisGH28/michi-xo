package com.luigidev.michixo.mobile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.model.GameResult
import com.luigidev.michixo.mobile.presentation.theme.ThemeType
import com.luigidev.michixo.domain.AiPlayer
import com.luigidev.michixo.domain.GameEngine
import com.luigidev.michixo.domain.Rules
import com.luigidev.michixo.model.Player
import com.luigidev.michixo_core.domain.SuperGatoEngine
import com.luigidev.michixo_core.domain.SuperGatoState
import com.luigidev.michixo_core.domain.SuperMove
import com.luigidev.michixo_core.model.Difficulty
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val engine: GameEngine = GameEngine(),
    private val superEngine: SuperGatoEngine = SuperGatoEngine()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(board = engine.newBoard()))
    val uiState: StateFlow<GameUiState> = _uiState

    var onGameFinished: ((GameResult) -> Unit)? = null

    fun setDifficulty(difficulty: Difficulty) {
        _uiState.update { state ->
            state.copy(
                difficulty = difficulty,
                opponent = opponentForDifficulty(difficulty)
            )
        }
    }

    fun setThemeType(themeType: ThemeType) {
        _uiState.update { state ->
            val opponent = opponentForTheme(themeType) ?: state.opponent
            state.copy(
                selectedThemeType = themeType,
                opponent = opponent,
                difficulty = opponent.difficulty
            )
        }
    }

    fun startGame() {
        val state = _uiState.value

        _uiState.value = GameUiState(
            screen = Screen.GAME,
            gameMode = GameMode.CLASSIC,
            board = engine.newBoard(),
            superGato = superEngine.newState(),
            currentTurn = Player.X,
            difficulty = state.difficulty,
            opponent = state.opponent,
            selectedThemeType = ThemeType.Luz,
            musicEnabled = state.musicEnabled,
            vibrationEnabled = state.vibrationEnabled,
            notificationsEnabled = state.notificationsEnabled,
            hasSeenSuperMichiIntro = state.hasSeenSuperMichiIntro,
            resultTitle = "",
            resultMessage = "",
            resultImageRes = null,
            resultMessageRes = null
        )
    }

    fun showSuperGatoIntro() {
        _uiState.update { state ->
            state.copy(
                screen = Screen.SUPER_INTRO,
                gameMode = GameMode.SUPER_GATO,
                opponent = opponentForDifficulty(state.difficulty),
                selectedThemeType = themeForOpponent(opponentForDifficulty(state.difficulty))
            )
        }
    }

    fun startSuperGato(opponent: CatOpponent) {
        _uiState.update { state ->
            state.copy(
                screen = Screen.GAME,
                gameMode = GameMode.SUPER_GATO,
                board = engine.newBoard(),
                superGato = superEngine.newState(),
                currentTurn = Player.X,
                difficulty = opponent.difficulty,
                opponent = opponent,
                selectedThemeType = themeForOpponent(opponent),
                showSuperGreeting = !state.hasSeenSuperMichiIntro,
                winner = null,
                winLine = null,
                isDraw = false,
                isAiThinking = false,
                resultTitle = "",
                resultMessage = "",
                resultImageRes = null,
                resultMessageRes = null
            )
        }
    }

    fun showSuperGatoTutorial() {
        _uiState.update { state ->
            state.copy(screen = Screen.SUPER_TUTORIAL, gameMode = GameMode.SUPER_GATO)
        }
    }

    fun showHomeFamilyGreeting() {
        _uiState.update { state ->
            state.copy(showHomeFamilyGreeting = true)
        }
    }

    fun dismissHomeFamilyGreeting() {
        _uiState.update { state ->
            state.copy(showHomeFamilyGreeting = false)
        }
    }

    fun showSuperFamilyGreeting() {
        _uiState.update { state ->
            state.copy(showSuperFamilyGreeting = true)
        }
    }

    fun dismissSuperFamilyGreeting() {
        _uiState.update { state ->
            state.copy(showSuperFamilyGreeting = false)
        }
    }

    fun dismissSuperGreeting() {
        _uiState.update { state ->
            state.copy(showSuperGreeting = false)
        }
    }

    fun setHasSeenSuperMichiIntro(seen: Boolean) {
        _uiState.update { state ->
            state.copy(
                hasSeenSuperMichiIntro = seen,
                showSuperGreeting = if (seen) false else state.showSuperGreeting
            )
        }
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
            gameMode = state.gameMode,
            difficulty = state.difficulty,
            opponent = state.opponent,
            musicEnabled = state.musicEnabled,
            vibrationEnabled = state.vibrationEnabled,
            notificationsEnabled = state.notificationsEnabled,
            selectedThemeType = state.selectedThemeType,
            hasSeenSuperMichiIntro = state.hasSeenSuperMichiIntro
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

    fun onSuperCellTap(boardIndex: Int, cellIndex: Int) {
        var stateAfterHuman: SuperGatoState? = null

        _uiState.update { state ->
            if (state.screen != Screen.GAME || state.gameMode != GameMode.SUPER_GATO) return@update state
            if (state.showSuperGreeting) return@update state
            if (state.superGato.winner != null || state.superGato.isDraw) return@update state
            if (state.isAiThinking) return@update state

            val next = superEngine.makeMove(
                state = state.superGato,
                move = SuperMove(boardIndex, cellIndex),
                player = Player.X
            )
            if (next == state.superGato) return@update state

            if (next.winner != null || next.isDraw) {
                onGameFinished?.invoke(
                    when {
                        next.isDraw -> GameResult.DRAW
                        next.winner == Player.X -> GameResult.WIN
                        else -> GameResult.NONE
                    }
                )

                return@update state.copy(
                    superGato = next,
                    board = next.boards,
                    winner = next.winner,
                    winLine = next.winLine,
                    isDraw = next.isDraw,
                    currentTurn = Player.X,
                    isAiThinking = false,
                    screen = Screen.RESULT,
                    resultImageRes = when {
                        next.isDraw -> superResultImage(state.opponent, GameResult.DRAW)
                        next.winner == Player.X -> superResultImage(state.opponent, GameResult.WIN)
                        else -> null
                    },
                    resultMessageRes = when {
                        next.isDraw -> superResultMessage(state.opponent, GameResult.DRAW)
                        next.winner == Player.X -> superResultMessage(state.opponent, GameResult.WIN)
                        else -> null
                    }
                )
            }

            stateAfterHuman = next

            state.copy(
                superGato = next,
                board = next.boards,
                currentTurn = Player.O,
                isAiThinking = true
            )
        }

        val baseState = stateAfterHuman ?: return

        viewModelScope.launch {
            delay(520)

            _uiState.update { state ->
                if (state.screen != Screen.GAME || state.gameMode != GameMode.SUPER_GATO) return@update state
                if (state.superGato.winner != null || state.superGato.isDraw) return@update state

                val aiMove = superEngine.chooseMove(baseState, Player.O, state.difficulty)
                    ?: return@update state.copy(isAiThinking = false)

                val next = superEngine.makeMove(baseState, aiMove, Player.O)

                if (next.winner != null || next.isDraw) {
                    onGameFinished?.invoke(
                        when {
                            next.isDraw -> GameResult.DRAW
                            next.winner == Player.O -> GameResult.LOSE
                            else -> GameResult.NONE
                        }
                    )
                }

                state.copy(
                    superGato = next,
                    board = next.boards,
                    winner = next.winner,
                    winLine = next.winLine,
                    isDraw = next.isDraw,
                    currentTurn = Player.X,
                    isAiThinking = false,
                    screen = if (next.winner != null || next.isDraw) Screen.RESULT else Screen.GAME,
                    resultImageRes = when {
                        next.isDraw -> superResultImage(state.opponent, GameResult.DRAW)
                        next.winner == Player.O -> superResultImage(state.opponent, GameResult.LOSE)
                        else -> state.resultImageRes
                    },
                    resultMessageRes = when {
                        next.isDraw -> superResultMessage(state.opponent, GameResult.DRAW)
                        next.winner == Player.O -> superResultMessage(state.opponent, GameResult.LOSE)
                        else -> state.resultMessageRes
                    }
                )
            }
        }
    }

    fun reset() {
        _uiState.update { state ->
            state.copy(
                board = if (state.gameMode == GameMode.SUPER_GATO) {
                    superEngine.newState().boards
                } else {
                    engine.newBoard()
                },
                superGato = superEngine.newState(),
                currentTurn = Player.X,
                winner = null,
                winLine = null,
                isDraw = false,
                isAiThinking = false,
                resultTitle = "",
                resultMessage = "",
                resultImageRes = null,
                resultMessageRes = null
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

    private fun opponentForDifficulty(difficulty: Difficulty): CatOpponent {
        return when (difficulty) {
            Difficulty.EASY -> CatOpponent.LILY
            Difficulty.MEDIUM -> CatOpponent.COCO
            Difficulty.HARD -> CatOpponent.SALEM
        }
    }

    private fun opponentForTheme(themeType: ThemeType): CatOpponent? {
        return when (themeType) {
            ThemeType.Luz -> null
            ThemeType.Lily -> CatOpponent.LILY
            ThemeType.Coco -> CatOpponent.COCO
            ThemeType.Salem -> CatOpponent.SALEM
        }
    }

    private fun themeForOpponent(opponent: CatOpponent): ThemeType {
        return when (opponent) {
            CatOpponent.LILY -> ThemeType.Lily
            CatOpponent.COCO -> ThemeType.Coco
            CatOpponent.SALEM -> ThemeType.Salem
        }
    }

    private fun superResultImage(opponent: CatOpponent, result: GameResult): Int {
        return when (opponent) {
            CatOpponent.LILY -> when (result) {
                GameResult.LOSE -> R.drawable.lily_winner
                GameResult.DRAW -> R.drawable.lily_draw
                GameResult.WIN -> R.drawable.lily_sad
                GameResult.NONE -> R.drawable.lily_draw
            }

            CatOpponent.COCO -> when (result) {
                GameResult.LOSE -> R.drawable.coco_winner
                GameResult.DRAW -> R.drawable.coco_draw
                GameResult.WIN -> R.drawable.coco_sad
                GameResult.NONE -> R.drawable.coco_draw
            }

            CatOpponent.SALEM -> when (result) {
                GameResult.LOSE -> R.drawable.salem_winner
                GameResult.DRAW -> R.drawable.salem_draw
                GameResult.WIN -> R.drawable.salem_sad
                GameResult.NONE -> R.drawable.salem_draw
            }
        }
    }

    private fun superResultMessage(opponent: CatOpponent, result: GameResult): Int {
        return when (opponent) {
            CatOpponent.LILY -> when (result) {
                GameResult.LOSE -> listOf(
                    R.string.result_msg_lily_wins,
                    R.string.result_msg_lily_wins_2,
                    R.string.result_msg_lily_wins_3
                ).random()
                GameResult.DRAW -> listOf(
                    R.string.result_msg_lily_draw,
                    R.string.result_msg_lily_draw_2,
                    R.string.result_msg_lily_draw_3
                ).random()
                GameResult.WIN -> listOf(
                    R.string.result_msg_lily_loses,
                    R.string.result_msg_lily_loses_2,
                    R.string.result_msg_lily_loses_3
                ).random()
                GameResult.NONE -> R.string.result_msg_lily_draw
            }

            CatOpponent.COCO -> when (result) {
                GameResult.LOSE -> listOf(
                    R.string.result_msg_coco_wins,
                    R.string.result_msg_coco_wins_2,
                    R.string.result_msg_coco_wins_3
                ).random()
                GameResult.DRAW -> listOf(
                    R.string.result_msg_coco_draw,
                    R.string.result_msg_coco_draw_2,
                    R.string.result_msg_coco_draw_3
                ).random()
                GameResult.WIN -> listOf(
                    R.string.result_msg_coco_loses,
                    R.string.result_msg_coco_loses_2,
                    R.string.result_msg_coco_loses_3
                ).random()
                GameResult.NONE -> R.string.result_msg_coco_draw
            }

            CatOpponent.SALEM -> when (result) {
                GameResult.LOSE -> listOf(
                    R.string.result_msg_salem_wins,
                    R.string.result_msg_salem_wins_2,
                    R.string.result_msg_salem_wins_3
                ).random()
                GameResult.DRAW -> listOf(
                    R.string.result_msg_salem_draw,
                    R.string.result_msg_salem_draw_2,
                    R.string.result_msg_salem_draw_3
                ).random()
                GameResult.WIN -> listOf(
                    R.string.result_msg_salem_loses,
                    R.string.result_msg_salem_loses_2,
                    R.string.result_msg_salem_loses_3
                ).random()
                GameResult.NONE -> R.string.result_msg_salem_draw
            }
        }
    }
}
