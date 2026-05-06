package com.luigidev.michixo.mobile.presentation

import com.luigidev.michixo.model.Player
import com.luigidev.michixo_core.domain.SuperGatoState
import com.luigidev.michixo_core.model.Difficulty

enum class Screen{
    HOME, GAME, SUPER_INTRO, SUPER_TUTORIAL, RESULT, SETTINGS
}

enum class GameMode {
    CLASSIC, SUPER_GATO
}

enum class CatOpponent(
    val difficulty: Difficulty
) {
    LILY(Difficulty.EASY),
    COCO(Difficulty.MEDIUM),
    SALEM(Difficulty.HARD)
}

data class GameUiState(
    val screen: Screen = Screen.HOME,
    val gameMode: GameMode = GameMode.CLASSIC,
    val board: List<Player> = List(9) { Player.NONE },
    val superGato: SuperGatoState = SuperGatoState(),

    val humanPlayer: Player = Player.X,
    val aiPlayer: Player = Player.O,

    val currentTurn: Player = Player.X,
    val isAiThinking: Boolean = false,

    val difficulty: Difficulty = Difficulty.EASY,
    val opponent: CatOpponent = CatOpponent.LILY,
    val showHomeFamilyGreeting: Boolean = false,
    val showSuperFamilyGreeting: Boolean = false,
    val showSuperGreeting: Boolean = false,

    val winner: Player? = null,
    val winLine: List<Int>? = null,
    val isDraw: Boolean = false,

    val resultTitle: String = "",
    val resultMessage: String = "",
    val resultImageRes: Int? = null,
    val resultMessageRes: Int? = null,

    val musicEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val notificationsEnabled: Boolean = false,
)
