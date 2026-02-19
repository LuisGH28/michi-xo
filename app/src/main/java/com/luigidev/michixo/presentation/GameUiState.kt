package com.luigidev.michixo.presentation

import com.luigidev.michixo.model.Player

data class GameUiState(
    val board: List<Player> = List(9) { Player.NONE },
    val currentTurn: Player = Player.X,
    val winner: Player? = null,
    val isDraw: Boolean = false
)
