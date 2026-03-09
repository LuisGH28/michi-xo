package com.luigidev.michixo.domain

import com.luigidev.michixo.model.Player

data class WinResult(
    val player: Player,
    val line: List<Int>
)

object Rules {

    private val winLines = listOf(
        listOf(0,1,2), listOf(3,4,5), listOf(6,7,8),
        listOf(0,3,6), listOf(1,4,7), listOf(2,5,8),
        listOf(0,4,8), listOf(2,4,6)
    )

    fun checkWinner(board: List<Player>): WinResult? {
        for (line in winLines) {
            val a = board[line[0]]
            if (a != Player.NONE &&
                a == board[line[1]] &&
                a == board[line[2]]
            ) {
                return WinResult(a, line)
            }
        }
        return null
    }

    fun isDraw(board: List<Player>) =
        board.all { it != Player.NONE } && checkWinner(board) == null
}
