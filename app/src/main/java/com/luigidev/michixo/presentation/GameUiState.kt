package com.luigidev.michixo.presentation

import com.luigidev.michixo.model.Player


enum class Screen {
    HOME, GAME
}
data class GameUiState(
    val screen: Screen = Screen.HOME,
    val board: List<Player> = List(9) { Player.NONE },
    val currentTurn: Player = Player.X,
    val isAiThinking: Boolean = false,
    val winner: Player? = null,
    val winLine: List<Int>? = null,
    val isDraw: Boolean = false
)
