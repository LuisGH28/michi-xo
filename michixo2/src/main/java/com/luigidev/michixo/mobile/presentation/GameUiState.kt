package com.luigidev.michixo.mobile.presentation

import com.luigidev.michixo.model.Player
import com.luigidev.michixo_core.model.Difficulty

enum class Screen {
    HOME, GAME, RESULT, SETTINGS
}

data class GameUiState(
    val screen: Screen = Screen.HOME,
    val board: List<Player> = List(9) { Player.NONE },

    val humanPlayer: Player = Player.X,
    val aiPlayer: Player = Player.O,

    val currentTurn: Player = Player.X,
    val isAiThinking: Boolean = false,

    val difficulty: Difficulty = Difficulty.EASY,
    val gameMode: GameMode = GameMode.HUMAN_VS_AI,

    val musicEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val notificationsEnabled: Boolean = false,

    val remoteAiConnected: Boolean = false,
    val remoteAiStatus: String = "",

    val winner: Player? = null,
    val winLine: List<Int>? = null,
    val isDraw: Boolean = false,

    val resultTitle: String = "",
    val resultMessage: String = "",
    val resultImageRes: Int? = null
)