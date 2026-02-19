package com.luigidev.michixo.domain

import com.luigidev.michixo.model.Player

class GameEngine {

    fun newBoard(): List<Player> = List(9) {Player.NONE}

    fun makeMove(board: List<Player>, index: Int, player: Player): List<Player> {
        if(index !in 0..8) return board
        if(board[index] != Player.NONE) return board

        val mutable = board.toMutableList()
        mutable[index] = player
        return mutable.toList()
    }
}