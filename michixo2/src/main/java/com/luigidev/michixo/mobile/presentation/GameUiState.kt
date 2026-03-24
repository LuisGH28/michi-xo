package com.luigidev.michixo.mobile.presentation

import com.luigidev.michixo.model.Player

enum class Screen{
    HOME, GAME, RESULT, SETTINGS
}

data class GameUiState(
    val screen: Screen = Screen.HOME,
    val board: List<Player> = List(9) { Player.NONE },

    val humanPlayer: Player = Player.X,
    val aiPlayer: Player = Player.O,

    val currentTurn: Player = Player.X,
    val isAiThinking: Boolean = false,

    val winner: Player? = null,
    val winLine: List<Int>? = null,
    val isDraw: Boolean = false,

    val resultTitle: String = "",
    val resultMessage: String = "",
    val resultImageRes: Int? = null
)