package com.luigidev.michixo.domain

import com.luigidev.michixo.model.Player

object Rules {

    private val winLines = listOf(
        listOf(0,1,2), listOf(3,4,5), listOf(6,7,8),
        listOf(0,3,6), listOf(1,4,7), listOf(2,5,8),
        listOf(0,4,8), listOf(2,4,6)
    )

    fun winner(board: List<Player>): Player? {
        for(line in winLines) {
            val a = board[line[0]]
            if(a != Player.NONE && a == board[line[1]] && a == board[line[2]]){
                return a
            }
        }
        return null
    }

    fun isDraw(board: List<Player>): Boolean =
        board.all { it != Player.NONE  } && winner(board) == null
}