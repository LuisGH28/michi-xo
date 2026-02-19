package com.luigidev.michixo.domain

import com.luigidev.michixo.model.Player

class AiPlayer {

    fun chooseMove(board: List<Player>, ai: Player): Int? {

        val opponent = if (ai == Player.X) Player.O else Player.X

        findWinningMove(board, ai)?.let { return it }

        findWinningMove(board, opponent)?.let { return it }

        val empty = board.withIndex()
            .filter { it.value == Player.NONE }
            .map { it.index }

        return empty.randomOrNull()
    }

    private fun findWinningMove(board: List<Player>, player: Player): Int? {
        for (i in board.indices) {
            if (board[i] == Player.NONE) {
                val copy = board.toMutableList()
                copy[i] = player
                if (Rules.checkWinner(copy) != null) {
                    return i
                }
            }
        }
        return null
    }
}
